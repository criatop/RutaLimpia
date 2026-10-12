package com.rutalimpia.solicitudes.dto;

import jakarta.validation.constraints.NotBlank;

public record TipoResiduoRequest(@NotBlank String nombre) {
}
