package com.rutalimpia.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank String nombre,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 6, message = "minimo 6 caracteres") String password,
        @NotBlank String rol,          // VECINO | CONDUCTOR
        Long camionId) {               // obligatorio si rol = CONDUCTOR
}
