package com.salesianos.dam.RegistroDeJornada.error;

import com.salesianos.dam.RegistroDeJornada.model.TipoFichaje;

public class FichajeDuplicadoException extends RuntimeException {

    public FichajeDuplicadoException(TipoFichaje tipo) {
        super("No se puede registrar una " + tipo + " porque el último fichaje del empleado ya es de ese tipo");
    }
}
