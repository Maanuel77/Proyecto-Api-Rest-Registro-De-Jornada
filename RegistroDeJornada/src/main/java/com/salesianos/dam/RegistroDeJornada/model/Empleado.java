package com.salesianos.dam.RegistroDeJornada.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Empleado {

    @Id @GeneratedValue
    private Long id;

    private String nombreCompleto;

    private String cargo;

    private BigDecimal salario;

    @ManyToOne
    private Departamento departamento;

    @OneToMany(mappedBy = "empleado")
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Fichaje> fichajes = new ArrayList<>();

}
