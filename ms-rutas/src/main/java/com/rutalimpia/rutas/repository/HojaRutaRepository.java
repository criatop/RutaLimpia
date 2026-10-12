package com.rutalimpia.rutas.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rutalimpia.rutas.model.HojaRuta;

public interface HojaRutaRepository extends JpaRepository<HojaRuta, Long> {
    Optional<HojaRuta> findByCamionIdAndFecha(Long camionId, LocalDate fecha);
}
