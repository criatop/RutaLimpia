package com.rutalimpia.flota.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rutalimpia.flota.dto.CamionRequest;
import com.rutalimpia.flota.dto.CamionResponse;
import com.rutalimpia.flota.exception.BadRequestException;
import com.rutalimpia.flota.exception.ConflictException;
import com.rutalimpia.flota.exception.NotFoundException;
import com.rutalimpia.flota.model.Camion;
import com.rutalimpia.flota.model.Zona;
import com.rutalimpia.flota.repository.CamionRepository;
import com.rutalimpia.flota.repository.ZonaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CamionService {

    private final CamionRepository camionRepository;
    private final ZonaRepository zonaRepository;

    public CamionResponse crear(CamionRequest req) {
        if (camionRepository.existsByPatente(req.patente())) {
            throw new ConflictException("Ya existe un camion con patente " + req.patente());
        }
        Camion c = new Camion();
        aplicar(c, req);
        return toResponse(camionRepository.save(c));
    }

    @Transactional(readOnly = true)
    public List<CamionResponse> listar() {
        return camionRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<CamionResponse> listarDisponibles() {
        return camionRepository.findByDisponibleTrue().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public CamionResponse obtener(Long id) {
        return toResponse(buscar(id));
    }

    public CamionResponse actualizar(Long id, CamionRequest req) {
        Camion c = buscar(id);
        if (!c.getPatente().equals(req.patente()) && camionRepository.existsByPatente(req.patente())) {
            throw new ConflictException("Ya existe un camion con patente " + req.patente());
        }
        if (req.capacidad() != null && req.capacidad() < c.getParadasAsignadas()) {
            throw new BadRequestException("La capacidad no puede ser menor que las paradas ya asignadas ("
                    + c.getParadasAsignadas() + ")");
        }
        aplicar(c, req);
        return toResponse(camionRepository.save(c));
    }

    public CamionResponse cambiarDisponibilidad(Long id, boolean disponible) {
        Camion c = buscar(id);
        c.setDisponible(disponible);
        return toResponse(camionRepository.save(c));
    }

    /** Reserva una parada en el camion. Cuando llega a su capacidad queda no disponible. */
    public CamionResponse asignarParada(Long id) {
        Camion c = buscar(id);
        if (!c.isDisponible()) {
            throw new ConflictException("El camion " + c.getPatente() + " no esta disponible");
        }
        c.setParadasAsignadas(c.getParadasAsignadas() + 1);
        if (c.getParadasAsignadas() >= c.getCapacidad()) {
            c.setDisponible(false);
        }
        return toResponse(camionRepository.save(c));
    }

    /**
     * Compensacion: libera una parada reservada. Se usa si el alta de la parada en ms-rutas
     * falla despues de reservar el cupo, para no dejar la capacidad consumida.
     */
    public CamionResponse liberarParada(Long id) {
        Camion c = buscar(id);
        if (c.getParadasAsignadas() > 0) {
            c.setParadasAsignadas(c.getParadasAsignadas() - 1);
        }
        if (c.getParadasAsignadas() < c.getCapacidad()) {
            c.setDisponible(true);
        }
        return toResponse(camionRepository.save(c));
    }

    public void eliminar(Long id) {
        camionRepository.delete(buscar(id));
    }

    private Camion buscar(Long id) {
        return camionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Camion no encontrado: " + id));
    }

    private void aplicar(Camion c, CamionRequest req) {
        c.setPatente(req.patente());
        c.setCapacidad(req.capacidad());
        if (req.zonaId() != null) {
            Zona z = zonaRepository.findById(req.zonaId())
                    .orElseThrow(() -> new NotFoundException("Zona no encontrada: " + req.zonaId()));
            c.setZona(z);
        } else {
            c.setZona(null);
        }
    }

    private CamionResponse toResponse(Camion c) {
        Zona z = c.getZona();
        return new CamionResponse(c.getId(), c.getPatente(), c.getCapacidad(), c.getParadasAsignadas(),
                c.isDisponible(), z == null ? null : z.getId(), z == null ? null : z.getNombre());
    }
}
