package com.salesianos.dam.RegistroDeJornada.error;

public class DepartamentoConEmpleadosException extends RuntimeException {

    public DepartamentoConEmpleadosException(Long id) {
        super("No se puede borrar el departamento con id " + id + " porque tiene empleados asignados");
    }
}
