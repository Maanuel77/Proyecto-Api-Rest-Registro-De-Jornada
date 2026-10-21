package com.salesianos.dam.RegistroDeJornada.error;

public class DepartamentoNotFoundException extends RuntimeException {

    public DepartamentoNotFoundException() {
        super("No se han encontrado departamentos");
    }

    public DepartamentoNotFoundException(Long id) {
        super("No se ha encontrado el departamento con id " + id);
    }

    public DepartamentoNotFoundException(String message) {
        super(message);
    }
}
