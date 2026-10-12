package com.rutalimpia.solicitudes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SolicitudRequest(
        @NotNull Long usuarioId,
        @NotBlank String direccion,
        @NotNull Long tipoResiduoId,
        @NotBlank String horarioPreferido) {   // ej: "09:00-12:00"
}
