package com.rutalimpia.rutas.model;

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
@Table(name = "paradas")
@Getter
@Setter
@NoArgsConstructor
public class Parada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Referencia logica a ms-solicitudes. Una solicitud solo puede estar en una parada. */
    @Column(nullable = false, unique = true)
    private Long solicitudId;

    @Column(nullable = false)
    private String direccion;

    private String tipoResiduo;

    private String horario;

    @Column(nullable = false)
    private int orden;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoParada estado = EstadoParada.PENDIENTE;

    private String observacion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hoja_ruta_id", nullable = false)
    private HojaRuta hojaRuta;
}
