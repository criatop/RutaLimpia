package com.rutalimpia.flota.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rutalimpia.flota.dto.ZonaRequest;
import com.rutalimpia.flota.exception.ConflictException;
import com.rutalimpia.flota.model.Zona;
import com.rutalimpia.flota.repository.ZonaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ZonaService {

    private final ZonaRepository zonaRepository;

    public List<Zona> listar() {
        return zonaRepository.findAll();
    }

    public Zona crear(ZonaRequest req) {
        if (zonaRepository.existsByNombre(req.nombre())) {
            throw new ConflictException("Ya existe la zona " + req.nombre());
        }
        return zonaRepository.save(new Zona(req.nombre()));
    }
}
