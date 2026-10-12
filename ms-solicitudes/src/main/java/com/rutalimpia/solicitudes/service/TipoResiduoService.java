package com.rutalimpia.solicitudes.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rutalimpia.solicitudes.dto.TipoResiduoRequest;
import com.rutalimpia.solicitudes.exception.ConflictException;
import com.rutalimpia.solicitudes.model.TipoResiduo;
import com.rutalimpia.solicitudes.repository.TipoResiduoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TipoResiduoService {

    private final TipoResiduoRepository repository;

    public List<TipoResiduo> listar() {
        return repository.findAll();
    }

    public TipoResiduo crear(TipoResiduoRequest req) {
        if (repository.existsByNombre(req.nombre())) {
            throw new ConflictException("Ya existe el tipo de residuo " + req.nombre());
        }
        return repository.save(new TipoResiduo(req.nombre()));
    }
}
