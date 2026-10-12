package com.rutalimpia.rutas.controller;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rutalimpia.rutas.dto.HojaRutaResponse;
import com.rutalimpia.rutas.dto.ParadaRequest;
import com.rutalimpia.rutas.dto.ParadaResponse;
import com.rutalimpia.rutas.dto.ResultadoRequest;
import com.rutalimpia.rutas.service.RutaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/rutas")
@RequiredArgsConstructor
public class RutaController {

    private final RutaService rutaService;

    /** Usado por el proceso de asignacion (Lambda). */
    @PostMapping("/paradas")
    public ResponseEntity<ParadaResponse> agregarParada(@Valid @RequestBody ParadaRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(rutaService.agregarParada(req));
    }

    /** Conductor: consulta las paradas asignadas a su camion. */
    @GetMapping("/camion/{camionId}")
    public HojaRutaResponse hojaDelCamion(
            @PathVariable Long camionId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return rutaService.obtenerHojaPorCamion(camionId, fecha);
    }

    @GetMapping("/paradas/{id}")
    public ParadaResponse obtenerParada(@PathVariable Long id) {
        return rutaService.obtenerParada(id);
    }

    /** Uso interno: parada asociada a una solicitud (para reintentos idempotentes). */
    @GetMapping("/paradas/solicitud/{solicitudId}")
    public ParadaResponse obtenerParadaPorSolicitud(@PathVariable Long solicitudId) {
        return rutaService.obtenerParadaPorSolicitud(solicitudId);
    }

    /** Conductor: registra si el retiro fue realizado o fallo. */
    @PutMapping("/paradas/{id}/resultado")
    public ParadaResponse resultado(@PathVariable Long id, @Valid @RequestBody ResultadoRequest req) {
        return rutaService.registrarResultado(id, req);
    }

    @DeleteMapping("/paradas/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        rutaService.eliminarParada(id);
        return ResponseEntity.noContent().build();
    }
}
