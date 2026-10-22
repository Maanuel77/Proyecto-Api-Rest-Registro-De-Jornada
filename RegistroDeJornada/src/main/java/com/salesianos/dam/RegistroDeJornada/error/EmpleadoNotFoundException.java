package com.salesianos.dam.RegistroDeJornada.error;

public class EmpleadoNotFoundException extends RuntimeException {

    public EmpleadoNotFoundException() {
        super("No se han encontrado empleados");
    }

    public EmpleadoNotFoundException(Long id) {
        super("No se ha encontrado el empleado con id " + id);
    }

    public EmpleadoNotFoundException(String message) {
        super(message);
    }
}
