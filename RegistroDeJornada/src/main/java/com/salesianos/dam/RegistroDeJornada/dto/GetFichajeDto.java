package com.salesianos.dam.RegistroDeJornada.dto;

import com.salesianos.dam.RegistroDeJornada.model.Fichaje;
import com.salesianos.dam.RegistroDeJornada.model.TipoFichaje;

import java.time.LocalDateTime;

public record GetFichajeDto(
        Long id,
        LocalDateTime momento,
        TipoFichaje tipo
) {

    public static GetFichajeDto of(Fichaje f) {
        return new GetFichajeDto(f.getId(), f.getMomento(), f.getTipo());
    }

}
