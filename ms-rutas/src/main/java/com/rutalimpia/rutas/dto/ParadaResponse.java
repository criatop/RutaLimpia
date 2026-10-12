package com.rutalimpia.rutas.dto;

import com.rutalimpia.rutas.model.EstadoParada;

public record ParadaResponse(Long id, Long solicitudId, Long camionId, String direccion, String tipoResiduo,
                             String horario, int orden, EstadoParada estado, String observacion) {
}
