package com.salesianos.dam.RegistroDeJornada.controller;

import com.salesianos.dam.RegistroDeJornada.dto.EditEmpleadoDto;
import com.salesianos.dam.RegistroDeJornada.dto.GetEmpleadoDetail;
import com.salesianos.dam.RegistroDeJornada.dto.GetEmpleadoList;
import com.salesianos.dam.RegistroDeJornada.model.Empleado;
import com.salesianos.dam.RegistroDeJornada.service.EmpleadoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/empleados")
public class EmpleadoController {

    private final EmpleadoService empleadoService;

    @GetMapping
    public ResponseEntity<List<GetEmpleadoList>> getAll() {

        List<Empleado> result = empleadoService.findAll();
        return ResponseEntity.ok(
                result
                        .stream()
                        .map(GetEmpleadoList::of)
                        .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetEmpleadoDetail> getById(@PathVariable Long id) {

        return ResponseEntity.ok(GetEmpleadoDetail.of(empleadoService.findById(id)));

    }

    @PostMapping
    public ResponseEntity<GetEmpleadoDetail> create(@Valid @RequestBody EditEmpleadoDto empleado) {

        return ResponseEntity.status(201)
                .body(GetEmpleadoDetail.of(empleadoService.create(empleado)));

    }

    @PutMapping("/{id}")
    public ResponseEntity<GetEmpleadoDetail> edit(
            @PathVariable Long id,
            @Valid @RequestBody EditEmpleadoDto empleado) {

        return ResponseEntity.ok(
                GetEmpleadoDetail.of(empleadoService.edit(id, empleado)));

    }

    @PutMapping("/{empleadoId}/departamento/{deptoId}")
    public ResponseEntity<GetEmpleadoDetail> asignarDepartamento(
            @PathVariable Long empleadoId,
            @PathVariable Long deptoId) {

        return ResponseEntity.ok(
                GetEmpleadoDetail.of(empleadoService.asignarDepartamento(empleadoId, deptoId)));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        empleadoService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
