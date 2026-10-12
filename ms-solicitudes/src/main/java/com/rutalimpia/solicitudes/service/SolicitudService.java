package com.rutalimpia.solicitudes.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rutalimpia.solicitudes.dto.EstadoRequest;
import com.rutalimpia.solicitudes.dto.SolicitudRequest;
import com.rutalimpia.solicitudes.dto.SolicitudResponse;
import com.rutalimpia.solicitudes.dto.SolicitudUpdateRequest;
import com.rutalimpia.solicitudes.exception.ConflictException;
import com.rutalimpia.solicitudes.exception.NotFoundException;
import com.rutalimpia.solicitudes.messaging.AsignacionPublisher;
import com.rutalimpia.solicitudes.model.EstadoSolicitud;
import com.rutalimpia.solicitudes.model.SolicitudRetiro;
import com.rutalimpia.solicitudes.model.TipoResiduo;
import com.rutalimpia.solicitudes.repository.SolicitudRepository;
import com.rutalimpia.solicitudes.repository.TipoResiduoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class SolicitudService {

    private final SolicitudRepository solicitudRepository;
    private final TipoResiduoRepository tipoResiduoRepository;
    private final AsignacionPublisher asignacionPublisher;

    /**
     * Registra la solicitud de inmediato (estado PENDIENTE) y devuelve el folio de recepcion.
     * La asignacion del camion ocurre despues, de forma asincrona, sin bloquear al vecino.
     */
    public SolicitudResponse crear(SolicitudRequest req) {
        TipoResiduo tipo = tipoResiduoRepository.findById(req.tipoResiduoId())
                .orElseThrow(() -> new NotFoundException("Tipo de residuo no encontrado: " + req.tipoResiduoId()));

        SolicitudRetiro s = new SolicitudRetiro();
        s.setFolio(generarFolio());
        s.setUsuarioId(req.usuarioId());
        s.setDireccion(req.direccion());
        s.setHorarioPreferido(req.horarioPreferido());
        s.setTipoResiduo(tipo);
        s.setEstado(EstadoSolicitud.PENDIENTE);
        s = solicitudRepository.save(s);

        asignacionPublisher.publicar(s.getFolio());
        return toResponse(s);
    }

    @Transactional(readOnly = true)
    public SolicitudResponse obtenerPorFolio(String folio) {
        return toResponse(solicitudRepository.findByFolio(folio)
                .orElseThrow(() -> new NotFoundException("Folio no encontrado: " + folio)));
    }

    @Transactional(readOnly = true)
    public List<SolicitudResponse> listar(Long usuarioId) {
        List<SolicitudRetiro> lista = usuarioId == null
                ? solicitudRepository.findAll()
                : solicitudRepository.findByUsuarioIdOrderByFechaCreacionDesc(usuarioId);
        return lista.stream().map(this::toResponse).toList();
    }

    public SolicitudResponse actualizar(Long id, SolicitudUpdateRequest req) {
        SolicitudRetiro s = buscar(id);
        if (s.getEstado() != EstadoSolicitud.PENDIENTE) {
            throw new ConflictException("Solo se pueden modificar solicitudes PENDIENTES");
        }
        s.setDireccion(req.direccion());
        s.setHorarioPreferido(req.horarioPreferido());
        return toResponse(solicitudRepository.save(s));
    }

    public SolicitudResponse cambiarEstado(Long id, EstadoRequest req) {
        SolicitudRetiro s = buscar(id);
        s.setEstado(req.estado());
        if (req.camionId() != null) {
            s.setCamionId(req.camionId());
        }
        return toResponse(solicitudRepository.save(s));
    }

    /** Usado por el worker de asignacion. */
    public void marcarAsignada(Long id, Long camionId) {
        SolicitudRetiro s = buscar(id);
        s.setEstado(EstadoSolicitud.ASIGNADA);
        s.setCamionId(camionId);
        solicitudRepository.save(s);
    }

    public void cancelar(Long id) {
        SolicitudRetiro s = buscar(id);
        if (s.getEstado() != EstadoSolicitud.PENDIENTE) {
            throw new ConflictException("Solo se pueden cancelar solicitudes PENDIENTES");
        }
        solicitudRepository.delete(s);
    }

    private SolicitudRetiro buscar(Long id) {
        return solicitudRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Solicitud no encontrada: " + id));
    }

    private String generarFolio() {
        String fecha = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        return "RL-" + fecha + "-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }

    private SolicitudResponse toResponse(SolicitudRetiro s) {
        return new SolicitudResponse(s.getId(), s.getFolio(), s.getUsuarioId(), s.getDireccion(),
                s.getTipoResiduo().getNombre(), s.getHorarioPreferido(), s.getEstado(),
                s.getCamionId(), s.getFechaCreacion());
    }
}
