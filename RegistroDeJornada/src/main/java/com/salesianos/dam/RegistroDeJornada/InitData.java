package com.salesianos.dam.RegistroDeJornada;

import com.salesianos.dam.RegistroDeJornada.model.Departamento;
import com.salesianos.dam.RegistroDeJornada.model.Empleado;
import com.salesianos.dam.RegistroDeJornada.model.Fichaje;
import com.salesianos.dam.RegistroDeJornada.model.TipoFichaje;
import com.salesianos.dam.RegistroDeJornada.repo.DepartamentoRepository;
import com.salesianos.dam.RegistroDeJornada.repo.EmpleadoRepository;
import com.salesianos.dam.RegistroDeJornada.repo.FichajeRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InitData {

    private final DepartamentoRepository departamentoRepository;
    private final EmpleadoRepository empleadoRepository;
    private final FichajeRepository fichajeRepository;

    @PostConstruct
    public void init() {
        Departamento rrhh = Departamento.builder().nombre("Recursos Humanos").presupuesto(new BigDecimal("50000.00")).build();
        Departamento desarrollo = Departamento.builder().nombre("Desarrollo").presupuesto(new BigDecimal("120000.00")).build();
        Departamento ventas = Departamento.builder().nombre("Ventas").presupuesto(new BigDecimal("80000.00")).build();

        departamentoRepository.saveAll(List.of(rrhh, desarrollo, ventas));

        Empleado laura = Empleado.builder().nombreCompleto("Laura García Pérez").cargo("Técnica de RRHH").salario(new BigDecimal("28000.00")).departamento(rrhh).build();
        Empleado carlos = Empleado.builder().nombreCompleto("Carlos Ruiz Martín").cargo("Desarrollador Backend").salario(new BigDecimal("35000.00")).departamento(desarrollo).build();
        Empleado ana = Empleado.builder().nombreCompleto("Ana López Sánchez").cargo("Desarrolladora Frontend").salario(new BigDecimal("33000.00")).departamento(desarrollo).build();
        Empleado javier = Empleado.builder().nombreCompleto("Javier Moreno Díaz").cargo("Comercial").salario(new BigDecimal("26000.00")).departamento(ventas).build();

        empleadoRepository.saveAll(List.of(laura, carlos, ana, javier));

        LocalDateTime ayer = LocalDateTime.now().minusDays(1).withHour(8).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime hoy = LocalDateTime.now().withHour(8).withMinute(0).withSecond(0).withNano(0);

        fichajeRepository.saveAll(List.of(
                Fichaje.builder().empleado(laura).tipo(TipoFichaje.ENTRADA).momento(ayer).build(),
                Fichaje.builder().empleado(laura).tipo(TipoFichaje.SALIDA).momento(ayer.plusHours(8)).build(),
                Fichaje.builder().empleado(carlos).tipo(TipoFichaje.ENTRADA).momento(ayer.plusMinutes(30)).build(),
                Fichaje.builder().empleado(carlos).tipo(TipoFichaje.SALIDA).momento(ayer.plusHours(8).plusMinutes(30)).build(),
                Fichaje.builder().empleado(carlos).tipo(TipoFichaje.ENTRADA).momento(hoy).build(),
                Fichaje.builder().empleado(ana).tipo(TipoFichaje.ENTRADA).momento(hoy.plusMinutes(15)).build()
        ));
    }

}
