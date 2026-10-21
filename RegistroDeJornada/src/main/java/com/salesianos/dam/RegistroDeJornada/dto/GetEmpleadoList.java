package com.salesianos.dam.RegistroDeJornada.dto;

import com.salesianos.dam.RegistroDeJornada.model.Empleado;

public record GetEmpleadoList(
        Long id,
        String nombreCompleto,
        String cargo,
        String nombreDepartamento
) {

    public static GetEmpleadoList of(Empleado e) {
        return new GetEmpleadoList(
                e.getId(),
                e.getNombreCompleto(),
                e.getCargo(),
                e.getDepartamento() != null ? e.getDepartamento().getNombre() : null
        );
    }

}
