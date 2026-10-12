package com.rutalimpia.rutas.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ParadaRequest(
        @NotNull Long solicitudId,
        @NotNull Long camionId,
        @NotBlank String direccion,
        String tipoResiduo,
        String horario,
        LocalDate fecha) {   // opcional: por defecto hoy
}
