package com.salesianos.dam.RegistroDeJornada.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PresupuestoSuficienteValidator.class)
@Documented
public @interface PresupuestoSuficiente {

    String message() default "{empleado.presupuesto.excedido}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
