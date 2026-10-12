package com.rutalimpia.solicitudes.dto;

import java.time.LocalDateTime;

import com.rutalimpia.solicitudes.model.EstadoSolicitud;

public record SolicitudResponse(Long id, String folio, Long usuarioId, String direccion, String tipoResiduo,
                                String horarioPreferido, EstadoSolicitud estado, Long camionId,
                                LocalDateTime fechaCreacion) {
}
