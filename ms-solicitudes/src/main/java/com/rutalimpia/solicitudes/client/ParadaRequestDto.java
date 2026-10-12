package com.rutalimpia.solicitudes.client;

public record ParadaRequestDto(Long solicitudId, Long camionId, String direccion, String tipoResiduo, String horario) {
}
