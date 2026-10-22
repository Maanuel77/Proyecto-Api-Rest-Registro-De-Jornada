package com.salesianos.dam.RegistroDeJornada.error;

public class DepartamentoDuplicadoException extends RuntimeException {

    public DepartamentoDuplicadoException(String nombre) {
        super("Ya existe un departamento con el nombre " + nombre);
    }
}
