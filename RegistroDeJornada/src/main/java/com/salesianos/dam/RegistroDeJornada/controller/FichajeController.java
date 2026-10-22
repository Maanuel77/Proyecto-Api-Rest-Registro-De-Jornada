package com.salesianos.dam.RegistroDeJornada.controller;

import com.salesianos.dam.RegistroDeJornada.dto.EditFichajeDto;
import com.salesianos.dam.RegistroDeJornada.dto.GetFichajeDto;
import com.salesianos.dam.RegistroDeJornada.service.FichajeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/empleados/{empleadoId}")
public class FichajeController {

    private final FichajeService fichajeService;

    @PostMapping("/fichar")
    public ResponseEntity<GetFichajeDto> fichar(
            @PathVariable Long empleadoId,
            @Valid @RequestBody EditFichajeDto fichaje) {

        return ResponseEntity.status(201)
                .body(GetFichajeDto.of(fichajeService.fichar(empleadoId, fichaje)));

    }

}
