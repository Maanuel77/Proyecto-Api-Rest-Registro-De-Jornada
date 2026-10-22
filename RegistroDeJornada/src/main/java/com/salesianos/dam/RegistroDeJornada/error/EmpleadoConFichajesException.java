package com.salesianos.dam.RegistroDeJornada.error;

import lombok.Getter;

@Getter
public class EmpleadoConFichajesException extends RuntimeException {

    private final Object[] args;

    public EmpleadoConFichajesException(Long id) {
        super("empleado.confichajes");
        this.args = new Object[]{id};
    }
}
