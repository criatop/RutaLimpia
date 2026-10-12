package com.rutalimpia.flota.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.rutalimpia.flota.model.Camion;
import com.rutalimpia.flota.model.Zona;
import com.rutalimpia.flota.repository.CamionRepository;
import com.rutalimpia.flota.repository.ZonaRepository;

import lombok.RequiredArgsConstructor;

/** Datos de ejemplo para poder probar de inmediato. */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ZonaRepository zonaRepository;
    private final CamionRepository camionRepository;

    @Override
    public void run(String... args) {
        if (zonaRepository.count() > 0) {
            return;
        }
        Zona norte = zonaRepository.save(new Zona("Norte"));
        Zona sur = zonaRepository.save(new Zona("Sur"));
        crearCamion("AB1234", 3, norte);
        crearCamion("CD5678", 3, sur);
    }

    private void crearCamion(String patente, int capacidad, Zona zona) {
        Camion c = new Camion();
        c.setPatente(patente);
        c.setCapacidad(capacidad);
        c.setZona(zona);
        camionRepository.save(c);
    }
}
