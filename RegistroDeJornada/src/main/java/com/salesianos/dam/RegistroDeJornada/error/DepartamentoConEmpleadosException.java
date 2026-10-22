package com.salesianos.dam.RegistroDeJornada.error;

import lombok.Getter;

@Getter
public class DepartamentoConEmpleadosException extends RuntimeException {

    private final Object[] args;

    public DepartamentoConEmpleadosException(Long id) {
        super("departamento.conempleados");
        this.args = new Object[]{id};
    }
}
