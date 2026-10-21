package com.salesianos.dam.RegistroDeJornada.dto;

import com.salesianos.dam.RegistroDeJornada.model.Departamento;

import java.math.BigDecimal;

public record GetDepartamentoList(
        Long id,
        String nombre,
        BigDecimal presupuesto,
        int numEmpleados
) {

    public static GetDepartamentoList of(Departamento d) {
        return new GetDepartamentoList(
                d.getId(),
                d.getNombre(),
                d.getPresupuesto(),
                d.getEmpleados().size()
        );
    }

}
