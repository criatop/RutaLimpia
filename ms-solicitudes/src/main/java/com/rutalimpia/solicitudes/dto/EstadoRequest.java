package com.rutalimpia.solicitudes.dto;

import com.rutalimpia.solicitudes.model.EstadoSolicitud;

import jakarta.validation.constraints.NotNull;

public record EstadoRequest(@NotNull EstadoSolicitud estado, Long camionId) {
}
