package com.rutalimpia.auth.dto;

public record TokenInfo(boolean valido, Long usuarioId, String email, String rol, Long camionId) {
}
