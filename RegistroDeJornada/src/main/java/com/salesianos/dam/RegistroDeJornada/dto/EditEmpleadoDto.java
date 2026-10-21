package com.salesianos.dam.RegistroDeJornada.dto;

import com.salesianos.dam.RegistroDeJornada.model.Empleado;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record EditEmpleadoDto(
        @NotBlank
        String nombreCompleto,
        @NotBlank
        String cargo,
        @NotNull
        @Positive
        BigDecimal salario,
        @NotNull
        Long departamentoId
) {

    public Empleado to() {
        return Empleado.builder()
                .nombreCompleto(nombreCompleto)
                .cargo(cargo)
                .salario(salario)
                .build();
    }

}
