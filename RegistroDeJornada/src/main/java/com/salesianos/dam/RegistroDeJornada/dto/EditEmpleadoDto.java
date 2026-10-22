package com.salesianos.dam.RegistroDeJornada.dto;

import com.salesianos.dam.RegistroDeJornada.model.Empleado;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record EditEmpleadoDto(
        @NotBlank(message = "{empleado.nombrecompleto.notblank}")
        @Size(max = 150, message = "{empleado.nombrecompleto.size}")
        String nombreCompleto,
        @NotBlank(message = "{empleado.cargo.notblank}")
        @Size(max = 100, message = "{empleado.cargo.size}")
        String cargo,
        @NotNull(message = "{empleado.salario.notnull}")
        @Positive(message = "{empleado.salario.positive}")
        @Digits(integer = 10, fraction = 2, message = "{empleado.salario.digits}")
        BigDecimal salario,
        @NotNull(message = "{empleado.departamentoid.notnull}")
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
