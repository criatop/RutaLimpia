package com.rutalimpia.auth.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.rutalimpia.auth.model.Rol;
import com.rutalimpia.auth.repository.RolRepository;

import lombok.RequiredArgsConstructor;

/** Crea los roles base al iniciar si no existen. */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RolRepository rolRepository;

    @Override
    public void run(String... args) {
        for (String nombre : List.of("VECINO", "CONDUCTOR")) {
            if (rolRepository.findByNombre(nombre).isEmpty()) {
                rolRepository.save(new Rol(nombre));
            }
        }
    }
}
