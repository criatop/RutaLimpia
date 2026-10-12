package com.rutalimpia.solicitudes.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.rutalimpia.solicitudes.client.CamionDto;
import com.rutalimpia.solicitudes.client.FlotaClient;
import com.rutalimpia.solicitudes.client.ParadaDto;
import com.rutalimpia.solicitudes.client.ParadaRequestDto;
import com.rutalimpia.solicitudes.client.RutasClient;
import com.rutalimpia.solicitudes.model.EstadoSolicitud;
import com.rutalimpia.solicitudes.model.SolicitudRetiro;
import com.rutalimpia.solicitudes.repository.SolicitudRepository;

import lombok.RequiredArgsConstructor;

/**
 * Simula la funcion AWS Lambda que consume la cola SQS:
 *  1. toma solicitudes PENDIENTES
 *  2. consulta disponibilidad a ms-flota
 *  3. agrega la parada a la hoja de ruta en ms-rutas
 *  4. marca la solicitud como ASIGNADA
 * Si no hay camion o un servicio no responde, la solicitud sigue PENDIENTE y se reintenta en la siguiente corrida.
 */
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "rutalimpia.worker.enabled", havingValue = "true", matchIfMissing = true)
public class AsignacionWorker {

    private static final Logger log = LoggerFactory.getLogger(AsignacionWorker.class);

    private final SolicitudRepository solicitudRepository;
    private final SolicitudService solicitudService;
    private final FlotaClient flotaClient;
    private final RutasClient rutasClient;

    @Scheduled(fixedDelayString = "${rutalimpia.worker.delay-ms:10000}")
    public void procesarPendientes() {
        List<SolicitudRetiro> pendientes =
                solicitudRepository.findByEstadoOrderByFechaCreacionAsc(EstadoSolicitud.PENDIENTE);
        if (pendientes.isEmpty()) {
            return;
        }
        log.info("[WORKER] {} solicitud(es) pendiente(s)", pendientes.size());

        for (SolicitudRetiro s : pendientes) {
            try {
                if (procesar(s)) {
                    break; // sin camiones disponibles: no tiene sentido seguir con el resto
                }
            } catch (Exception e) {
                // Un fallo en una solicitud no debe abortar las demas.
                log.warn("[WORKER] No se pudo asignar {} (se reintentara). Detalle: {}", s.getFolio(), e.getMessage());
            }
        }
    }

    /** @return true si no hay camiones disponibles (conviene detener el lote). */
    private boolean procesar(SolicitudRetiro s) {
        // Idempotencia: si la parada ya existe (p.ej. reintento tras un timeout) solo marcamos la solicitud.
        Optional<ParadaDto> existente = rutasClient.buscarPorSolicitud(s.getId());
        if (existente.isPresent() && existente.get().camionId() != null) {
            solicitudService.marcarAsignada(s.getId(), existente.get().camionId());
            log.info("[WORKER] {} ya tenia parada creada; marcada como ASIGNADA", s.getFolio());
            return false;
        }

        Optional<CamionDto> camion = flotaClient.primerCamionDisponible();
        if (camion.isEmpty()) {
            log.warn("[WORKER] Sin camiones disponibles. {} sigue PENDIENTE", s.getFolio());
            return true;
        }

        Long camionId = camion.get().id();
        flotaClient.reservarParada(camionId);
        try {
            rutasClient.agregarParada(new ParadaRequestDto(s.getId(), camionId, s.getDireccion(),
                    s.getTipoResiduo().getNombre(), s.getHorarioPreferido()));
        } catch (RuntimeException e) {
            // Compensacion: si no se creo la parada, devolvemos el cupo reservado en ms-flota.
            flotaClient.liberarParada(camionId);
            throw e;
        }
        solicitudService.marcarAsignada(s.getId(), camionId);
        log.info("[WORKER] {} asignada al camion {}", s.getFolio(), camion.get().patente());
        return false;
    }
}
