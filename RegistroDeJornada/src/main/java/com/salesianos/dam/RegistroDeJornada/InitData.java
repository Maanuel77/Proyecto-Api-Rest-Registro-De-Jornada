package com.salesianos.dam.RegistroDeJornada;

import com.salesianos.dam.RegistroDeJornada.model.Departamento;
import com.salesianos.dam.RegistroDeJornada.repo.DepartamentoRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InitData {

    private final DepartamentoRepository departamentoRepository;

    @PostConstruct
    public void init() {
        departamentoRepository.saveAll(List.of(
                Departamento.builder().nombre("Recursos Humanos").presupuesto(new BigDecimal("50000.00")).build(),
                Departamento.builder().nombre("Desarrollo").presupuesto(new BigDecimal("120000.00")).build(),
                Departamento.builder().nombre("Ventas").presupuesto(new BigDecimal("80000.00")).build()
        ));
    }

}
