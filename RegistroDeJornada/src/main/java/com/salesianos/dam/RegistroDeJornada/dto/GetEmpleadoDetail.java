package com.salesianos.dam.RegistroDeJornada.dto;

import com.salesianos.dam.RegistroDeJornada.model.Empleado;

import java.math.BigDecimal;

public record GetEmpleadoDetail(
        Long id,
        String nombreCompleto,
        String cargo,
        BigDecimal salario,
        Long departamentoId,
        String nombreDepartamento
) {

    public static GetEmpleadoDetail of(Empleado e) {
        return new GetEmpleadoDetail(
                e.getId(),
                e.getNombreCompleto(),
                e.getCargo(),
                e.getSalario(),
                e.getDepartamento() != null ? e.getDepartamento().getId() : null,
                e.getDepartamento() != null ? e.getDepartamento().getNombre() : null
        );
    }

}
