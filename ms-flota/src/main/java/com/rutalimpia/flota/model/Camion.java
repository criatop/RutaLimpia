package com.rutalimpia.flota.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "camiones")
@Getter
@Setter
@NoArgsConstructor
public class Camion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    private String patente;

    /** Maximo de paradas que puede tener asignadas. */
    @Column(nullable = false)
    private int capacidad;

    @Column(nullable = false)
    private int paradasAsignadas = 0;

    @Column(nullable = false)
    private boolean disponible = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zona_id")
    private Zona zona;
}
