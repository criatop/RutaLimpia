package com.rutalimpia.flota.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rutalimpia.flota.dto.ZonaRequest;
import com.rutalimpia.flota.model.Zona;
import com.rutalimpia.flota.service.ZonaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/zonas")
@RequiredArgsConstructor
public class ZonaController {

    private final ZonaService zonaService;

    @GetMapping
    public List<Zona> listar() {
        return zonaService.listar();
    }

    @PostMapping
    public ResponseEntity<Zona> crear(@Valid @RequestBody ZonaRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(zonaService.crear(req));
    }
}
