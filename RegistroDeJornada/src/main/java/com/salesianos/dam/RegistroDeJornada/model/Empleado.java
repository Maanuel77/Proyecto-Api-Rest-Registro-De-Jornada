package com.salesianos.dam.RegistroDeJornada.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Empleado {

    @Id @GeneratedValue
    private Long id;

    private String nombre;

    @ManyToOne
    private Departamento departamento;

}
