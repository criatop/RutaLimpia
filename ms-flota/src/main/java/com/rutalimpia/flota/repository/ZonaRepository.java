package com.rutalimpia.flota.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rutalimpia.flota.model.Zona;

public interface ZonaRepository extends JpaRepository<Zona, Long> {
    boolean existsByNombre(String nombre);
}
