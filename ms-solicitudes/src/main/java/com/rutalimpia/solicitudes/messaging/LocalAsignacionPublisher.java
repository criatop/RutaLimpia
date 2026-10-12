package com.rutalimpia.solicitudes.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Implementacion local: la "cola" es la propia tabla (solicitudes en estado PENDIENTE)
 * y el worker las consume. Solo registra el evento en el log.
 * Para pasar a SQS real basta reemplazar esta clase por una que envie el mensaje a la cola.
 */
@Component
public class LocalAsignacionPublisher implements AsignacionPublisher {

    private static final Logger log = LoggerFactory.getLogger(LocalAsignacionPublisher.class);

    @Override
    public void publicar(String folio) {
        log.info("[COLA] Solicitud {} encolada para asignacion de camion", folio);
    }
}
