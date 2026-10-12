package com.rutalimpia.rutas.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rutalimpia.rutas.dto.HojaRutaResponse;
import com.rutalimpia.rutas.dto.ParadaRequest;
import com.rutalimpia.rutas.dto.ParadaResponse;
import com.rutalimpia.rutas.dto.ResultadoRequest;
import com.rutalimpia.rutas.exception.BadRequestException;
import com.rutalimpia.rutas.exception.ConflictException;
import com.rutalimpia.rutas.exception.NotFoundException;
import com.rutalimpia.rutas.model.EstadoParada;
import com.rutalimpia.rutas.model.HojaRuta;
import com.rutalimpia.rutas.model.Parada;
import com.rutalimpia.rutas.repository.HojaRutaRepository;
import com.rutalimpia.rutas.repository.ParadaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class RutaService {

    private final HojaRutaRepository hojaRutaRepository;
    private final ParadaRepository paradaRepository;

    /** Incorpora un retiro a la hoja de ruta del camion (la crea si no existe para esa fecha). */
    public ParadaResponse agregarParada(ParadaRequest req) {
        if (paradaRepository.existsBySolicitudId(req.solicitudId())) {
            throw new ConflictException("La solicitud " + req.solicitudId() + " ya esta en una hoja de ruta");
        }
        LocalDate fecha = req.fecha() != null ? req.fecha() : LocalDate.now();
        HojaRuta hoja = hojaRutaRepository.findByCamionIdAndFecha(req.camionId(), fecha)
                .orElseGet(() -> hojaRutaRepository.save(new HojaRuta(req.camionId(), fecha)));

        Parada p = new Parada();
        p.setSolicitudId(req.solicitudId());
        p.setDireccion(req.direccion());
        p.setTipoResiduo(req.tipoResiduo());
        p.setHorario(req.horario());
        p.setOrden(hoja.getParadas().size() + 1);
        p.setHojaRuta(hoja);
        hoja.getParadas().add(p);
        return toResponse(paradaRepository.save(p));
    }

    /** Hoja de ruta del conductor (por camion). */
    @Transactional(readOnly = true)
    public HojaRutaResponse obtenerHojaPorCamion(Long camionId, LocalDate fecha) {
        LocalDate f = fecha != null ? fecha : LocalDate.now();
        HojaRuta hoja = hojaRutaRepository.findByCamionIdAndFecha(camionId, f)
                .orElseThrow(() -> new NotFoundException(
                        "No hay hoja de ruta para el camion " + camionId + " en la fecha " + f));
        return new HojaRutaResponse(hoja.getId(), hoja.getCamionId(), hoja.getFecha(),
                hoja.getParadas().stream().map(this::toResponse).toList());
    }

    @Transactional(readOnly = true)
    public ParadaResponse obtenerParada(Long id) {
        return toResponse(buscar(id));
    }

    /** Uso interno: permite reintentos idempotentes desde el proceso de asignacion. */
    @Transactional(readOnly = true)
    public ParadaResponse obtenerParadaPorSolicitud(Long solicitudId) {
        return toResponse(paradaRepository.findBySolicitudId(solicitudId)
                .orElseThrow(() -> new NotFoundException("No hay parada para la solicitud " + solicitudId)));
    }

    /** El conductor registra si el retiro fue REALIZADO o FALLIDO. */
    public ParadaResponse registrarResultado(Long id, ResultadoRequest req) {
        if (req.estado() == EstadoParada.PENDIENTE) {
            throw new BadRequestException("El resultado debe ser REALIZADO o FALLIDO");
        }
        Parada p = buscar(id);
        p.setEstado(req.estado());
        p.setObservacion(req.observacion());
        return toResponse(paradaRepository.save(p));
    }

    public void eliminarParada(Long id) {
        Parada p = buscar(id);
        p.getHojaRuta().getParadas().remove(p);
        paradaRepository.delete(p);
    }

    private Parada buscar(Long id) {
        return paradaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Parada no encontrada: " + id));
    }

    private ParadaResponse toResponse(Parada p) {
        return new ParadaResponse(p.getId(), p.getSolicitudId(), p.getHojaRuta().getCamionId(),
                p.getDireccion(), p.getTipoResiduo(), p.getHorario(), p.getOrden(), p.getEstado(), p.getObservacion());
    }
}
