package com.salesianos.dam.RegistroDeJornada.error;

import lombok.Getter;

@Getter
public class EmpleadoNotFoundException extends RuntimeException {

    private final Object[] args;

    public EmpleadoNotFoundException() {
        super("empleado.notfound.list");
        this.args = new Object[]{};
    }

    public EmpleadoNotFoundException(Long id) {
        super("empleado.notfound.id");
        this.args = new Object[]{id};
    }
}
