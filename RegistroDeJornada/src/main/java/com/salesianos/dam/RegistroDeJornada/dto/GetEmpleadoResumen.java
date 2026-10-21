package com.salesianos.dam.RegistroDeJornada.dto;

import com.salesianos.dam.RegistroDeJornada.model.Empleado;

public record GetEmpleadoResumen(
        Long id,
        String nombreCompleto,
        String cargo
) {

    public static GetEmpleadoResumen of(Empleado e) {
        return new GetEmpleadoResumen(e.getId(), e.getNombreCompleto(), e.getCargo());
    }

}
