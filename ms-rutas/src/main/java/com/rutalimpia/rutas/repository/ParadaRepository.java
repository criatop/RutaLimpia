package com.rutalimpia.rutas.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rutalimpia.rutas.model.Parada;

public interface ParadaRepository extends JpaRepository<Parada, Long> {
    boolean existsBySolicitudId(Long solicitudId);

    Optional<Parada> findBySolicitudId(Long solicitudId);
}
