package com.salesianos.dam.RegistroDeJornada.error;

import lombok.Getter;

@Getter
public class DepartamentoDuplicadoException extends RuntimeException {

    private final Object[] args;

    public DepartamentoDuplicadoException(String nombre) {
        super("departamento.duplicado");
        this.args = new Object[]{nombre};
    }
}
