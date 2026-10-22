package com.salesianos.dam.RegistroDeJornada.service;

import com.salesianos.dam.RegistroDeJornada.dto.EditDepartamentoDto;
import com.salesianos.dam.RegistroDeJornada.error.DepartamentoConEmpleadosException;
import com.salesianos.dam.RegistroDeJornada.error.DepartamentoDuplicadoException;
import com.salesianos.dam.RegistroDeJornada.error.DepartamentoNotFoundException;
import com.salesianos.dam.RegistroDeJornada.model.Departamento;
import com.salesianos.dam.RegistroDeJornada.repo.DepartamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        if (departamentoRepository.existsByNombre(editDepartamentoDto.nombre())) {
            throw new DepartamentoDuplicadoException(editDepartamentoDto.nombre());
        }
        return departamentoRepository.save(editDepartamentoDto.to());
    }

    public Departamento edit(Long id, EditDepartamentoDto editDepartamentoDto) {

        if (departamentoRepository.existsByNombreAndIdNot(editDepartamentoDto.nombre(), id)) {
            throw new DepartamentoDuplicadoException(editDepartamentoDto.nombre());
        }

        return departamentoRepository.findById(id)
                .map(d -> {
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

}
