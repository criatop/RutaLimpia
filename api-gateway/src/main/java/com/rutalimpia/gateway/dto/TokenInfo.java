package com.rutalimpia.gateway.dto;

/** Respuesta de ms-auth en GET /auth/validate. */
public record TokenInfo(boolean valido, Long usuarioId, String email, String rol, Long camionId) {
}
