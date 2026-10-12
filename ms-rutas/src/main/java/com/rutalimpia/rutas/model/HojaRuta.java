package com.rutalimpia.rutas.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "hojas_ruta", uniqueConstraints = @UniqueConstraint(columnNames = {"camion_id", "fecha"}))
@Getter
@Setter
@NoArgsConstructor
public class HojaRuta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Referencia logica a ms-flota. */
    @Column(name = "camion_id", nullable = false)
    private Long camionId;

    @Column(nullable = false)
    private LocalDate fecha;

    @OneToMany(mappedBy = "hojaRuta", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("orden ASC")
    private List<Parada> paradas = new ArrayList<>();

    public HojaRuta(Long camionId, LocalDate fecha) {
        this.camionId = camionId;
        this.fecha = fecha;
    }
}
