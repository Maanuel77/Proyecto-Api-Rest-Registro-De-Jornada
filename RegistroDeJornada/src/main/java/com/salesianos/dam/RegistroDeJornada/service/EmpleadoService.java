package com.salesianos.dam.RegistroDeJornada.service;

import com.salesianos.dam.RegistroDeJornada.dto.EditEmpleadoDto;
import com.salesianos.dam.RegistroDeJornada.error.DepartamentoNotFoundException;
import com.salesianos.dam.RegistroDeJornada.error.EmpleadoConFichajesException;
import com.salesianos.dam.RegistroDeJornada.error.EmpleadoNotFoundException;
import com.salesianos.dam.RegistroDeJornada.model.Departamento;
import com.salesianos.dam.RegistroDeJornada.model.Empleado;
import com.salesianos.dam.RegistroDeJornada.repo.DepartamentoRepository;
import com.salesianos.dam.RegistroDeJornada.repo.EmpleadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmpleadoService {

    private final EmpleadoRepository empleadoRepository;
    private final DepartamentoRepository departamentoRepository;

    public List<Empleado> findAll() {
        List<Empleado> result = empleadoRepository.findAll();
        if (result.isEmpty()) {
            throw new EmpleadoNotFoundException();
        }
        return result;
    }

    public Empleado findById(Long id) {
        return empleadoRepository.findById(id)
                .orElseThrow(() -> new EmpleadoNotFoundException(id));
    }

    public Empleado create(EditEmpleadoDto editEmpleadoDto) {
        Empleado empleado = editEmpleadoDto.to();
        empleado.setDepartamento(getDepartamento(editEmpleadoDto.departamentoId()));
        return empleadoRepository.save(empleado);
    }

    public Empleado edit(Long id, EditEmpleadoDto editEmpleadoDto) {

        return empleadoRepository.findById(id)
                .map(e -> {
                    e.setNombreCompleto(editEmpleadoDto.nombreCompleto());
                    e.setCargo(editEmpleadoDto.cargo());
                    e.setSalario(editEmpleadoDto.salario());
                    e.setDepartamento(getDepartamento(editEmpleadoDto.departamentoId()));
                    return empleadoRepository.save(e);
                })
                .orElseThrow(() -> new EmpleadoNotFoundException(id));
    }

    public Empleado asignarDepartamento(Long empleadoId, Long deptoId) {
        Empleado empleado = findById(empleadoId);
        empleado.setDepartamento(getDepartamento(deptoId));
        return empleadoRepository.save(empleado);
    }

    public void delete(Long id) {
        empleadoRepository.findById(id)
                .ifPresent(e -> {
                    if (!e.getFichajes().isEmpty()) {
                        throw new EmpleadoConFichajesException(id);
                    }
                    empleadoRepository.delete(e);
                });
    }

    private Departamento getDepartamento(Long departamentoId) {
        return departamentoRepository.findById(departamentoId)
                .orElseThrow(() -> new DepartamentoNotFoundException(departamentoId));
    }

}
