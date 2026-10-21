package com.salesianos.dam.RegistroDeJornada.dto;

import com.salesianos.dam.RegistroDeJornada.model.Departamento;

import java.math.BigDecimal;
import java.util.List;

public record GetDepartamentoDetail(
        Long id,
        String nombre,
        BigDecimal presupuesto,
        List<GetEmpleadoResumen> empleados
) {

    public static GetDepartamentoDetail of(Departamento d) {
        return new GetDepartamentoDetail(
                d.getId(),
                d.getNombre(),
                d.getPresupuesto(),
                d.getEmpleados().stream()
                        .map(GetEmpleadoResumen::of)
                        .toList()
        );
    }

}
