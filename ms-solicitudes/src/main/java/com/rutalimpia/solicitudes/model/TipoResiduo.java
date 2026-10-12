package com.rutalimpia.solicitudes.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tipos_residuo")
@Getter
@Setter
@NoArgsConstructor
public class TipoResiduo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 60)
    private String nombre;

    @JsonIgnore
    @OneToMany(mappedBy = "tipoResiduo")
    private List<SolicitudRetiro> solicitudes = new ArrayList<>();

    public TipoResiduo(String nombre) {
        this.nombre = nombre;
    }
}
