package com.rutalimpia.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record UsuarioUpdateRequest(@NotBlank String nombre, Long camionId) {
}
