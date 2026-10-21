package com.salesianos.dam.RegistroDeJornada.controller;

import com.salesianos.dam.RegistroDeJornada.dto.EditDepartamentoDto;
import com.salesianos.dam.RegistroDeJornada.dto.GetDepartamentoDetail;
import com.salesianos.dam.RegistroDeJornada.dto.GetDepartamentoList;
import com.salesianos.dam.RegistroDeJornada.model.Departamento;
import com.salesianos.dam.RegistroDeJornada.service.DepartamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/departamentos")
public class DepartamentoController {

    private final DepartamentoService departamentoService;

    @GetMapping
    public ResponseEntity<List<GetDepartamentoList>> getAll() {

        List<Departamento> result = departamentoService.findAll();
        return ResponseEntity.ok(
                result
                        .stream()
                        .map(GetDepartamentoList::of)
                        .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetDepartamentoDetail> getById(@PathVariable Long id) {

        return ResponseEntity.ok(GetDepartamentoDetail.of(departamentoService.findById(id)));

    }

    @PostMapping
    public ResponseEntity<GetDepartamentoDetail> create(@Valid @RequestBody EditDepartamentoDto departamento) {

        return ResponseEntity.status(201)
                .body(GetDepartamentoDetail.of(departamentoService.create(departamento)));

    }

    @PutMapping("/{id}")
    public ResponseEntity<GetDepartamentoDetail> edit(
            @PathVariable Long id,
            @Valid @RequestBody EditDepartamentoDto departamento) {

        return ResponseEntity.ok(
                GetDepartamentoDetail.of(departamentoService.edit(id, departamento)));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        departamentoService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
