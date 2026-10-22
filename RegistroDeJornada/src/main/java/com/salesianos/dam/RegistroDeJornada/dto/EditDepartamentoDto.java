package com.salesianos.dam.RegistroDeJornada.dto;

import com.salesianos.dam.RegistroDeJornada.model.Departamento;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record EditDepartamentoDto(
        @NotBlank(message = "{departamento.nombre.notblank}")
        @Size(max = 100, message = "{departamento.nombre.size}")
        String nombre,
        @NotNull(message = "{departamento.presupuesto.notnull}")
        @Positive(message = "{departamento.presupuesto.positive}")
        @Digits(integer = 10, fraction = 2, message = "{departamento.presupuesto.digits}")
        BigDecimal presupuesto
) {

    public Departamento to() {
        return Departamento.builder()
                .nombre(nombre)
                .presupuesto(presupuesto)
                .build();
    }

}
