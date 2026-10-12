package com.rutalimpia.rutas.dto;

import java.time.LocalDate;
import java.util.List;

public record HojaRutaResponse(Long id, Long camionId, LocalDate fecha, List<ParadaResponse> paradas) {
}
