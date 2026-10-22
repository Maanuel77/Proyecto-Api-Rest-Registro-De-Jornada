package com.salesianos.dam.RegistroDeJornada.error;

import lombok.Getter;

@Getter
public class FichajeNotFoundException extends RuntimeException {

    private final Object[] args;

    public FichajeNotFoundException(Long empleadoId) {
        super("fichaje.notfound.empleado");
        this.args = new Object[]{empleadoId};
    }
}
