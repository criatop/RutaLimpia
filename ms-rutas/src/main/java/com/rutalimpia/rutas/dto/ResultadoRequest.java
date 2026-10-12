package com.rutalimpia.rutas.dto;

import com.rutalimpia.rutas.model.EstadoParada;

import jakarta.validation.constraints.NotNull;

public record ResultadoRequest(@NotNull EstadoParada estado, String observacion) {
}
