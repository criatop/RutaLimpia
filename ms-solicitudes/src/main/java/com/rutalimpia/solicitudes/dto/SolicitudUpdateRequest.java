package com.rutalimpia.solicitudes.dto;

import jakarta.validation.constraints.NotBlank;

public record SolicitudUpdateRequest(@NotBlank String direccion, @NotBlank String horarioPreferido) {
}
