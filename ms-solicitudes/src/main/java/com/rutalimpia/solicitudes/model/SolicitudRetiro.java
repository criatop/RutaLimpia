package com.rutalimpia.solicitudes.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "solicitudes_retiro")
@Getter
@Setter
@NoArgsConstructor
public class SolicitudRetiro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Folio de recepcion que se entrega al vecino al confirmar. */
    @Column(nullable = false, unique = true, length = 30)
    private String folio;

    /** Referencia logica a ms-auth. */
    @Column(nullable = false)
    private Long usuarioId;

    @Column(nullable = false)
    private String direccion;

    @Column(nullable = false, length = 50)
    private String horarioPreferido;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoSolicitud estado = EstadoSolicitud.PENDIENTE;

    /** Referencia logica a ms-flota; se completa cuando se asigna. */
    private Long camionId;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tipo_residuo_id", nullable = false)
    private TipoResiduo tipoResiduo;
}
