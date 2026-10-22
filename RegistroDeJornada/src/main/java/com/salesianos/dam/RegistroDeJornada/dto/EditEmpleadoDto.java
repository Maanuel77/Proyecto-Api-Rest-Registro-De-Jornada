package com.salesianos.dam.RegistroDeJornada.dto;

import com.salesianos.dam.RegistroDeJornada.model.Empleado;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record EditEmpleadoDto(
        @NotBlank
        @Size(max = 150)
        String nombreCompleto,
        @NotBlank
        @Size(max = 100)
        String cargo,
        @NotNull
        @Positive
        @Digits(integer = 10, fraction = 2)
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
