package com.salesianos.dam.RegistroDeJornada.error;

import com.salesianos.dam.RegistroDeJornada.model.TipoFichaje;
import lombok.Getter;

@Getter
public class FichajeDuplicadoException extends RuntimeException {

    private final Object[] args;

    public FichajeDuplicadoException(TipoFichaje tipo) {
        super("fichaje.duplicado");
        this.args = new Object[]{tipo};
    }
}
