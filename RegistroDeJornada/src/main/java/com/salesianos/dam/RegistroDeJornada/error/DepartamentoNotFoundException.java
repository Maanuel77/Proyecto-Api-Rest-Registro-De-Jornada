package com.salesianos.dam.RegistroDeJornada.error;

import lombok.Getter;

@Getter
public class DepartamentoNotFoundException extends RuntimeException {

    private final Object[] args;

    public DepartamentoNotFoundException() {
        super("departamento.notfound.list");
        this.args = new Object[]{};
    }

    public DepartamentoNotFoundException(Long id) {
        super("departamento.notfound.id");
        this.args = new Object[]{id};
    }
}
