package com.rutalimpia.solicitudes.client;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/** Cliente HTTP hacia ms-flota (resuelto por Eureka). */
@Component
public class FlotaClient {

    private final RestClient restClient;

    public FlotaClient(RestClient.Builder builder, @Value("${servicios.flota.url}") String baseUrl) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(2000);
        factory.setReadTimeout(3000);
        this.restClient = builder.clone().baseUrl(baseUrl).requestFactory(factory).build();
    }

    public Optional<CamionDto> primerCamionDisponible() {
        List<CamionDto> lista = restClient.get().uri("/camiones/disponibles").retrieve()
                .body(new ParameterizedTypeReference<List<CamionDto>>() { });
        return lista == null || lista.isEmpty() ? Optional.empty() : Optional.of(lista.get(0));
    }

    public void reservarParada(Long camionId) {
        restClient.put().uri("/camiones/{id}/asignar-parada", camionId).retrieve().toBodilessEntity();
    }

    /** Compensacion: devuelve el cupo reservado si la parada no se pudo crear. */
    public void liberarParada(Long camionId) {
        restClient.put().uri("/camiones/{id}/liberar-parada", camionId).retrieve().toBodilessEntity();
    }
}
