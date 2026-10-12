package com.rutalimpia.solicitudes.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.rutalimpia.solicitudes.model.EstadoSolicitud;
import com.rutalimpia.solicitudes.model.SolicitudRetiro;

public interface SolicitudRepository extends JpaRepository<SolicitudRetiro, Long> {

    @EntityGraph(attributePaths = "tipoResiduo")
    Optional<SolicitudRetiro> findByFolio(String folio);

    @EntityGraph(attributePaths = "tipoResiduo")
    List<SolicitudRetiro> findByUsuarioIdOrderByFechaCreacionDesc(Long usuarioId);

    @EntityGraph(attributePaths = "tipoResiduo")
    List<SolicitudRetiro> findByEstadoOrderByFechaCreacionAsc(EstadoSolicitud estado);

    @Override
    @EntityGraph(attributePaths = "tipoResiduo")
    List<SolicitudRetiro> findAll();

    @Override
    @EntityGraph(attributePaths = "tipoResiduo")
    Optional<SolicitudRetiro> findById(Long id);
}
