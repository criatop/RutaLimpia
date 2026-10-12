package com.rutalimpia.solicitudes.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rutalimpia.solicitudes.dto.EstadoRequest;
import com.rutalimpia.solicitudes.dto.SolicitudRequest;
import com.rutalimpia.solicitudes.dto.SolicitudResponse;
import com.rutalimpia.solicitudes.dto.SolicitudUpdateRequest;
import com.rutalimpia.solicitudes.service.SolicitudService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/solicitudes")
@RequiredArgsConstructor
public class SolicitudController {

    private final SolicitudService solicitudService;

    /** Vecino: crea una solicitud. Responde 201 con el folio; la asignacion es asincrona. */
    @PostMapping
    public ResponseEntity<SolicitudResponse> crear(@Valid @RequestBody SolicitudRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(solicitudService.crear(req));
    }

    /** Vecino: consulta por folio. */
    @GetMapping("/folio/{folio}")
    public SolicitudResponse porFolio(@PathVariable String folio) {
        return solicitudService.obtenerPorFolio(folio);
    }

    /** Vecino: lista sus solicitudes (?usuarioId=). Sin parametro lista todas. */
    @GetMapping
    public List<SolicitudResponse> listar(@RequestParam(required = false) Long usuarioId) {
        return solicitudService.listar(usuarioId);
    }

    @PutMapping("/{id}")
    public SolicitudResponse actualizar(@PathVariable Long id, @Valid @RequestBody SolicitudUpdateRequest req) {
        return solicitudService.actualizar(id, req);
    }

    @PutMapping("/{id}/estado")
    public SolicitudResponse cambiarEstado(@PathVariable Long id, @Valid @RequestBody EstadoRequest req) {
        return solicitudService.cambiarEstado(id, req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        solicitudService.cancelar(id);
        return ResponseEntity.noContent().build();
    }
}
