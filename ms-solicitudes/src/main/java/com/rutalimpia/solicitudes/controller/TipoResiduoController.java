package com.rutalimpia.solicitudes.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rutalimpia.solicitudes.dto.TipoResiduoRequest;
import com.rutalimpia.solicitudes.model.TipoResiduo;
import com.rutalimpia.solicitudes.service.TipoResiduoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/tipos-residuo")
@RequiredArgsConstructor
public class TipoResiduoController {

    private final TipoResiduoService service;

    @GetMapping
    public List<TipoResiduo> listar() {
        return service.listar();
    }

    @PostMapping
    public ResponseEntity<TipoResiduo> crear(@Valid @RequestBody TipoResiduoRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(req));
    }
}
