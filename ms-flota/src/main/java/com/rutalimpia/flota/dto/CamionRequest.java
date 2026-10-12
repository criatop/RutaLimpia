package com.rutalimpia.flota.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CamionRequest(
        @NotBlank String patente,
        @NotNull @Min(value = 1, message = "debe ser al menos 1") Integer capacidad,
        Long zonaId) {
}
