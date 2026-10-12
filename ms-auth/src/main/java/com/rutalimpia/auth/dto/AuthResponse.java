package com.rutalimpia.auth.dto;

public record AuthResponse(String token, String tipo, Long usuarioId, String rol, Long camionId) {
}
