package com.salesianos.dam.RegistroDeJornada.error;

public class EmpleadoConFichajesException extends RuntimeException {

    public EmpleadoConFichajesException(Long id) {
        super("No se puede borrar el empleado con id " + id + " porque tiene fichajes registrados");
    }
}
