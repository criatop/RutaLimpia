package com.rutalimpia.solicitudes.client;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

/** Cliente HTTP hacia ms-rutas (resuelto por Eureka). */
@Component
public class RutasClient {

    private final RestClient restClient;

    public RutasClient(RestClient.Builder builder, @Value("${servicios.rutas.url}") String baseUrl) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(2000);
        factory.setReadTimeout(3000);
        this.restClient = builder.clone().baseUrl(baseUrl).requestFactory(factory).build();
    }

    public void agregarParada(ParadaRequestDto dto) {
        restClient.post().uri("/rutas/paradas").body(dto).retrieve().toBodilessEntity();
    }

    /** Devuelve la parada de una solicitud si ya existe (para reintentos idempotentes). */
    public Optional<ParadaDto> buscarPorSolicitud(Long solicitudId) {
        try {
            ParadaDto dto = restClient.get().uri("/rutas/paradas/solicitud/{id}", solicitudId)
                    .retrieve().body(ParadaDto.class);
            return Optional.ofNullable(dto);
        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        }
    }
}
