package com.salesianos.dam.RegistroDeJornada;

import com.salesianos.dam.RegistroDeJornada.model.Departamento;
import com.salesianos.dam.RegistroDeJornada.model.Empleado;
import com.salesianos.dam.RegistroDeJornada.repo.DepartamentoRepository;
import com.salesianos.dam.RegistroDeJornada.repo.EmpleadoRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InitData {

    private final DepartamentoRepository departamentoRepository;
    private final EmpleadoRepository empleadoRepository;

    @PostConstruct
    public void init() {
        Departamento rrhh = Departamento.builder().nombre("Recursos Humanos").presupuesto(new BigDecimal("50000.00")).build();
        Departamento desarrollo = Departamento.builder().nombre("Desarrollo").presupuesto(new BigDecimal("120000.00")).build();
        Departamento ventas = Departamento.builder().nombre("Ventas").presupuesto(new BigDecimal("80000.00")).build();

        departamentoRepository.saveAll(List.of(rrhh, desarrollo, ventas));

        empleadoRepository.saveAll(List.of(
                Empleado.builder().nombreCompleto("Laura García Pérez").cargo("Técnica de RRHH").salario(new BigDecimal("28000.00")).departamento(rrhh).build(),
                Empleado.builder().nombreCompleto("Carlos Ruiz Martín").cargo("Desarrollador Backend").salario(new BigDecimal("35000.00")).departamento(desarrollo).build(),
                Empleado.builder().nombreCompleto("Ana López Sánchez").cargo("Desarrolladora Frontend").salario(new BigDecimal("33000.00")).departamento(desarrollo).build(),
                Empleado.builder().nombreCompleto("Javier Moreno Díaz").cargo("Comercial").salario(new BigDecimal("26000.00")).departamento(ventas).build()
        ));
    }

}
