package com.salesianos.dam.RegistroDeJornada.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Fichaje {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private Empleado empleado;

}
