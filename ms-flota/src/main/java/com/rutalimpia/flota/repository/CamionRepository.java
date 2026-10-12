package com.rutalimpia.flota.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rutalimpia.flota.model.Camion;

public interface CamionRepository extends JpaRepository<Camion, Long> {
    boolean existsByPatente(String patente);

    List<Camion> findByDisponibleTrue();
}
