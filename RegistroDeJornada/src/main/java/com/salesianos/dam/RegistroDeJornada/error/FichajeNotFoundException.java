package com.salesianos.dam.RegistroDeJornada.error;

public class FichajeNotFoundException extends RuntimeException {

    public FichajeNotFoundException(Long empleadoId) {
        super("El empleado con id " + empleadoId + " no tiene fichajes");
    }

    public FichajeNotFoundException(String message) {
        super(message);
    }
}
