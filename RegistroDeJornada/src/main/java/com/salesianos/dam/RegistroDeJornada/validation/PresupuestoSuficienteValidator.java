package com.salesianos.dam.RegistroDeJornada.validation;

import com.salesianos.dam.RegistroDeJornada.dto.EditEmpleadoDto;
import com.salesianos.dam.RegistroDeJornada.model.Empleado;
import com.salesianos.dam.RegistroDeJornada.repo.DepartamentoRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;

import java.math.BigDecimal;
import java.util.Map;

@RequiredArgsConstructor
public class PresupuestoSuficienteValidator implements ConstraintValidator<PresupuestoSuficiente, EditEmpleadoDto> {

    private final DepartamentoRepository departamentoRepository;

    @Override
    public boolean isValid(EditEmpleadoDto dto, ConstraintValidatorContext context) {
        if (dto == null || dto.salario() == null || dto.departamentoId() == null) {
            return true;
        }

        Long empleadoId = getEmpleadoIdDeLaRuta();

        return departamentoRepository.findById(dto.departamentoId())
                .map(d -> d.getEmpleados().stream()
                        .filter(e -> !e.getId().equals(empleadoId))
                        .map(Empleado::getSalario)
                        .reduce(dto.salario(), BigDecimal::add)
                        .compareTo(d.getPresupuesto()) <= 0)
                .orElse(true);
    }

    /*
        Al editar (PUT /api/v1/empleados/{id}) se obtiene el id del empleado de la URL
        para no sumar dos veces su salario. Al crear no hay id y se devuelve null.
     */
    @SuppressWarnings("unchecked")
    private Long getEmpleadoIdDeLaRuta() {
        if (!(RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attributes)) {
            return null;
        }
        Map<String, String> variables = (Map<String, String>) attributes.getRequest()
                .getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        if (variables == null || variables.get("id") == null) {
            return null;
        }
        return Long.valueOf(variables.get("id"));
    }

}
