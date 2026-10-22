package com.salesianos.dam.RegistroDeJornada.dto;

import com.salesianos.dam.RegistroDeJornada.model.TipoFichaje;
import jakarta.validation.constraints.NotNull;

public record EditFichajeDto(
        @NotNull
        TipoFichaje tipo
) {
}
