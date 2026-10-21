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
public class Departamento {

    @Id @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String nombre;

    private BigDecimal presupuesto;

    @OneToMany(mappedBy = "departamento")
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Empleado> empleados = new ArrayList<>();

}
