package com.rutalimpia.solicitudes.messaging;

/**
 * Puerto de salida para encolar solicitudes de asignacion.
 * En AWS la implementacion real publicaria en Amazon SQS (ej. con SqsTemplate de Spring Cloud AWS).
 */
public interface AsignacionPublisher {
    void publicar(String folio);
}
