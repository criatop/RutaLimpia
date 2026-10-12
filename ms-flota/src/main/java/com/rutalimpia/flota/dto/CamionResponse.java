package com.rutalimpia.flota.dto;

public record CamionResponse(Long id, String patente, int capacidad, int paradasAsignadas,
                             boolean disponible, Long zonaId, String zonaNombre) {
}
