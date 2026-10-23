package com.salesianos.dam.RegistroDeJornada.service;

import com.salesianos.dam.RegistroDeJornada.dto.EditDepartamentoDto;
import com.salesianos.dam.RegistroDeJornada.error.DepartamentoConEmpleadosException;
import com.salesianos.dam.RegistroDeJornada.error.DepartamentoDuplicadoException;
import com.salesianos.dam.RegistroDeJornada.error.DepartamentoNotFoundException;
import com.salesianos.dam.RegistroDeJornada.error.PresupuestoExcedidoException;
import com.salesianos.dam.RegistroDeJornada.model.Departamento;
import com.salesianos.dam.RegistroDeJornada.model.Empleado;
import com.salesianos.dam.RegistroDeJornada.repo.DepartamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartamentoService {

    private final DepartamentoRepository departamentoRepository;

    public List<Departamento> findAll() {
        List<Departamento> result = departamentoRepository.findAll();
        if (result.isEmpty()) {
            throw new DepartamentoNotFoundException();
        }
        return result;
    }

    public Departamento findById(Long id) {
        return departamentoRepository.findById(id)
                .orElseThrow(() -> new DepartamentoNotFoundException(id));
    }

    public Departamento create(EditDepartamentoDto editDepartamentoDto) {
        if (departamentoRepository.existsByNombreIgnoreCase(editDepartamentoDto.nombre())) {
            throw new DepartamentoDuplicadoException(editDepartamentoDto.nombre());
        }
        return departamentoRepository.save(editDepartamentoDto.to());
    }

    public Departamento edit(Long id, EditDepartamentoDto editDepartamentoDto) {

        if (departamentoRepository.existsByNombreIgnoreCaseAndIdNot(editDepartamentoDto.nombre(), id)) {
            throw new DepartamentoDuplicadoException(editDepartamentoDto.nombre());
        }

        return departamentoRepository.findById(id)
                .map(d -> {
                    validarPresupuesto(d, editDepartamentoDto.presupuesto());
                    d.setNombre(editDepartamentoDto.nombre());
                    d.setPresupuesto(editDepartamentoDto.presupuesto());
                    return departamentoRepository.save(d);
                })
                .orElseThrow(() -> new DepartamentoNotFoundException(id));
    }

    public void delete(Long id) {
        departamentoRepository.findById(id)
                .ifPresent(d -> {
                    if (!d.getEmpleados().isEmpty()) {
                        throw new DepartamentoConEmpleadosException(id);
                    }
                    departamentoRepository.delete(d);
                });
    }

    private void validarPresupuesto(Departamento departamento, BigDecimal presupuesto) {
        BigDecimal totalSalarios = departamento.getEmpleados().stream()
                .map(Empleado::getSalario)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalSalarios.compareTo(presupuesto) > 0) {
            throw new PresupuestoExcedidoException(
                    totalSalarios, departamento.getNombre(), presupuesto);
        }
    }

}
