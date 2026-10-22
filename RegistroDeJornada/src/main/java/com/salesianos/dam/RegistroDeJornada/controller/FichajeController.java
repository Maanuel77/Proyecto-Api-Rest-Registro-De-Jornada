package com.salesianos.dam.RegistroDeJornada.controller;

import com.salesianos.dam.RegistroDeJornada.dto.EditFichajeDto;
import com.salesianos.dam.RegistroDeJornada.dto.GetFichajeDto;
import com.salesianos.dam.RegistroDeJornada.service.FichajeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/empleados/{empleadoId}")
@Tag(name = "Fichajes", description = "Registro de entradas y salidas de los empleados")
public class FichajeController {

    private final FichajeService fichajeService;

    @Operation(summary = "Registrar un fichaje",
            description = "Registra una ENTRADA o SALIDA del empleado con la hora actual. Los tipos deben alternarse.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Fichaje registrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetFichajeDto.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "id": 7,
                                        "momento": "2026-10-23T08:00:00",
                                        "tipo": "ENTRADA"
                                    }
                                    """))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada no válidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "No existe el empleado",
                    content = @Content(mediaType = "application/problem+json",
                            examples = @ExampleObject(value = """
                                    {
                                        "type": "https://example.com/errors/empleado-not-found",
                                        "title": "Recurso no encontrado",
                                        "status": 404,
                                        "detail": "No se ha encontrado el empleado con id 99",
                                        "instance": "/api/v1/empleados/99/fichar"
                                    }
                                    """))),
            @ApiResponse(responseCode = "409", description = "El tipo de fichaje se repite",
                    content = @Content(mediaType = "application/problem+json",
                            examples = @ExampleObject(value = """
                                    {
                                        "type": "https://example.com/errors/fichaje-duplicado",
                                        "title": "Fichaje duplicado",
                                        "status": 409,
                                        "detail": "No se puede registrar una ENTRADA porque el último fichaje del empleado ya es de ese tipo",
                                        "instance": "/api/v1/empleados/2/fichar"
                                    }
                                    """)))
    })
    @PostMapping("/fichar")
    public ResponseEntity<GetFichajeDto> fichar(
            @Parameter(description = "Id del empleado", example = "2")
            @PathVariable Long empleadoId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Tipo de fichaje",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EditFichajeDto.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "tipo": "ENTRADA"
                                    }
                                    """)))
            @Valid @RequestBody EditFichajeDto fichaje) {

        return ResponseEntity.status(201)
                .body(GetFichajeDto.of(fichajeService.fichar(empleadoId, fichaje)));

    }

    @Operation(summary = "Historial de fichajes",
            description = "Devuelve los fichajes del empleado ordenados del más reciente al más antiguo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Historial encontrado",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GetFichajeDto.class)),
                            examples = @ExampleObject(value = """
                                    [
                                        {
                                            "id": 5,
                                            "momento": "2026-10-23T08:00:00",
                                            "tipo": "ENTRADA"
                                        },
                                        {
                                            "id": 4,
                                            "momento": "2026-10-22T16:30:00",
                                            "tipo": "SALIDA"
                                        }
                                    ]
                                    """))),
            @ApiResponse(responseCode = "404", description = "No existe el empleado o no tiene fichajes",
                    content = @Content(mediaType = "application/problem+json",
                            examples = @ExampleObject(value = """
                                    {
                                        "type": "https://example.com/errors/fichaje-not-found",
                                        "title": "Recurso no encontrado",
                                        "status": 404,
                                        "detail": "El empleado con id 4 no tiene fichajes",
                                        "instance": "/api/v1/empleados/4/fichajes"
                                    }
                                    """)))
    })
    @GetMapping("/fichajes")
    public ResponseEntity<List<GetFichajeDto>> historial(
            @Parameter(description = "Id del empleado", example = "2")
            @PathVariable Long empleadoId) {

        return ResponseEntity.ok(
                fichajeService.historial(empleadoId)
                        .stream()
                        .map(GetFichajeDto::of)
                        .toList());
    }

}
