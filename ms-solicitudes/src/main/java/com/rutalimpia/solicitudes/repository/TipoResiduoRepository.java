package com.rutalimpia.solicitudes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rutalimpia.solicitudes.model.TipoResiduo;

public interface TipoResiduoRepository extends JpaRepository<TipoResiduo, Long> {
    boolean existsByNombre(String nombre);
}
