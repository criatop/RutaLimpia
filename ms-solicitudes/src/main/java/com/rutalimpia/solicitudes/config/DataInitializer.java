package com.rutalimpia.solicitudes.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.rutalimpia.solicitudes.model.TipoResiduo;
import com.rutalimpia.solicitudes.repository.TipoResiduoRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final TipoResiduoRepository repository;

    @Override
    public void run(String... args) {
        if (repository.count() == 0) {
            List.of("Vidrio", "Plastico", "Papel", "Carton", "Metal")
                    .forEach(n -> repository.save(new TipoResiduo(n)));
        }
    }
}
