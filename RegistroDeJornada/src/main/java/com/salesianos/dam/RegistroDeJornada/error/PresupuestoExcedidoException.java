package com.salesianos.dam.RegistroDeJornada.error;

import java.math.BigDecimal;
import lombok.Getter;

@Getter
public class PresupuestoExcedidoException extends RuntimeException {

    private final Object[] args;

    public PresupuestoExcedidoException(BigDecimal totalSalarios, String departamento, BigDecimal presupuesto) {
        super("presupuesto.excedido");
        this.args = new Object[]{totalSalarios, departamento, presupuesto};
    }
}
