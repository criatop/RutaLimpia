package com.rutalimpia.flota.dto;

import jakarta.validation.constraints.NotNull;

public record DisponibilidadRequest(@NotNull Boolean disponible) {
}
