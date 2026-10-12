package com.rutalimpia.flota.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rutalimpia.flota.dto.CamionRequest;
import com.rutalimpia.flota.dto.CamionResponse;
import com.rutalimpia.flota.dto.DisponibilidadRequest;
import com.rutalimpia.flota.service.CamionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/camiones")
@RequiredArgsConstructor
public class CamionController {

    private final CamionService camionService;

    @PostMapping
    public ResponseEntity<CamionResponse> crear(@Valid @RequestBody CamionRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(camionService.crear(req));
    }

    @GetMapping
    public List<CamionResponse> listar() {
        return camionService.listar();
    }

    @GetMapping("/disponibles")
    public List<CamionResponse> disponibles() {
        return camionService.listarDisponibles();
    }

    @GetMapping("/{id}")
    public CamionResponse obtener(@PathVariable Long id) {
        return camionService.obtener(id);
    }

    @PutMapping("/{id}")
    public CamionResponse actualizar(@PathVariable Long id, @Valid @RequestBody CamionRequest req) {
        return camionService.actualizar(id, req);
    }

    @PutMapping("/{id}/disponibilidad")
    public CamionResponse disponibilidad(@PathVariable Long id, @Valid @RequestBody DisponibilidadRequest req) {
        return camionService.cambiarDisponibilidad(id, req.disponible());
    }

    /** Lo invoca el proceso de asignacion (Lambda) al reservar una parada. */
    @PutMapping("/{id}/asignar-parada")
    public CamionResponse asignarParada(@PathVariable Long id) {
        return camionService.asignarParada(id);
    }

    /** Compensacion: libera la parada reservada si no se pudo crear en ms-rutas. */
    @PutMapping("/{id}/liberar-parada")
    public CamionResponse liberarParada(@PathVariable Long id) {
        return camionService.liberarParada(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        camionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
