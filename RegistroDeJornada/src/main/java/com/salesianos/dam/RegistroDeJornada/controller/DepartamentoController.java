package com.salesianos.dam.RegistroDeJornada.controller;

import com.salesianos.dam.RegistroDeJornada.dto.EditDepartamentoDto;
import com.salesianos.dam.RegistroDeJornada.dto.GetDepartamentoDetail;
import com.salesianos.dam.RegistroDeJornada.dto.GetDepartamentoList;
import com.salesianos.dam.RegistroDeJornada.model.Departamento;
import com.salesianos.dam.RegistroDeJornada.service.DepartamentoService;
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
@RequestMapping("/api/v1/departamentos")
@Tag(name = "Departamentos", description = "Gestión de los departamentos de la empresa")
public class DepartamentoController {

    private final DepartamentoService departamentoService;

    @Operation(summary = "Listar departamentos", description = "Devuelve todos los departamentos con su número de empleados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Departamentos encontrados",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GetDepartamentoList.class)),
                            examples = @ExampleObject(value = """
                                    [
                                        {
                                            "id": 1,
                                            "nombre": "Recursos Humanos",
                                            "presupuesto": 50000.00,
                                            "numEmpleados": 1
                                        },
                                        {
                                            "id": 2,
                                            "nombre": "Desarrollo",
                                            "presupuesto": 120000.00,
                                            "numEmpleados": 2
                                        }
                                    ]
                                    """))),
            @ApiResponse(responseCode = "404", description = "No hay departamentos",
                    content = @Content(mediaType = "application/problem+json",
                            examples = @ExampleObject(value = """
                                    {
                                        "type": "https://example.com/errors/departamento-not-found",
                                        "title": "Recurso no encontrado",
                                        "status": 404,
                                        "detail": "No se han encontrado departamentos",
                                        "instance": "/api/v1/departamentos"
                                    }
                                    """)))
    })
    @GetMapping
    public ResponseEntity<List<GetDepartamentoList>> getAll() {

        List<Departamento> result = departamentoService.findAll();
        return ResponseEntity.ok(
                result
                        .stream()
                        .map(GetDepartamentoList::of)
                        .toList());
    }

    @Operation(summary = "Obtener un departamento", description = "Devuelve el departamento con sus empleados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Departamento encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetDepartamentoDetail.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "id": 2,
                                        "nombre": "Desarrollo",
                                        "presupuesto": 120000.00,
                                        "empleados": [
                                            {
                                                "id": 2,
                                                "nombreCompleto": "Carlos Ruiz Martín",
                                                "cargo": "Desarrollador Backend"
                                            },
                                            {
                                                "id": 3,
                                                "nombreCompleto": "Ana López Sánchez",
                                                "cargo": "Desarrolladora Frontend"
                                            }
                                        ]
                                    }
                                    """))),
            @ApiResponse(responseCode = "404", description = "No existe el departamento",
                    content = @Content(mediaType = "application/problem+json",
                            examples = @ExampleObject(value = """
                                    {
                                        "type": "https://example.com/errors/departamento-not-found",
                                        "title": "Recurso no encontrado",
                                        "status": 404,
                                        "detail": "No se ha encontrado el departamento con id 99",
                                        "instance": "/api/v1/departamentos/99"
                                    }
                                    """)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<GetDepartamentoDetail> getById(
            @Parameter(description = "Id del departamento", example = "2")
            @PathVariable Long id) {

        return ResponseEntity.ok(GetDepartamentoDetail.of(departamentoService.findById(id)));

    }

    @Operation(summary = "Crear un departamento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Departamento creado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetDepartamentoDetail.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "id": 4,
                                        "nombre": "Marketing",
                                        "presupuesto": 40000.00,
                                        "empleados": []
                                    }
                                    """))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada no válidos", content = @Content),
            @ApiResponse(responseCode = "409", description = "Ya existe un departamento con ese nombre",
                    content = @Content(mediaType = "application/problem+json",
                            examples = @ExampleObject(value = """
                                    {
                                        "type": "https://example.com/errors/departamento-duplicado",
                                        "title": "Departamento duplicado",
                                        "status": 409,
                                        "detail": "Ya existe un departamento con el nombre Ventas",
                                        "instance": "/api/v1/departamentos"
                                    }
                                    """)))
    })
    @PostMapping
    public ResponseEntity<GetDepartamentoDetail> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del departamento",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EditDepartamentoDto.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "nombre": "Marketing",
                                        "presupuesto": 40000.00
                                    }
                                    """)))
            @Valid @RequestBody EditDepartamentoDto departamento) {

        return ResponseEntity.status(201)
                .body(GetDepartamentoDetail.of(departamentoService.create(departamento)));

    }

    @Operation(summary = "Editar un departamento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Departamento editado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetDepartamentoDetail.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "id": 3,
                                        "nombre": "Ventas",
                                        "presupuesto": 90000.00,
                                        "empleados": [
                                            {
                                                "id": 4,
                                                "nombreCompleto": "Javier Moreno Díaz",
                                                "cargo": "Comercial"
                                            }
                                        ]
                                    }
                                    """))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada no válidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "No existe el departamento",
                    content = @Content(mediaType = "application/problem+json",
                            examples = @ExampleObject(value = """
                                    {
                                        "type": "https://example.com/errors/departamento-not-found",
                                        "title": "Recurso no encontrado",
                                        "status": 404,
                                        "detail": "No se ha encontrado el departamento con id 99",
                                        "instance": "/api/v1/departamentos/99"
                                    }
                                    """))),
            @ApiResponse(responseCode = "409", description = "Ya existe otro departamento con ese nombre",
                    content = @Content(mediaType = "application/problem+json",
                            examples = @ExampleObject(value = """
                                    {
                                        "type": "https://example.com/errors/departamento-duplicado",
                                        "title": "Departamento duplicado",
                                        "status": 409,
                                        "detail": "Ya existe un departamento con el nombre Desarrollo",
                                        "instance": "/api/v1/departamentos/3"
                                    }
                                    """)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<GetDepartamentoDetail> edit(
            @Parameter(description = "Id del departamento", example = "3")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Nuevos datos del departamento",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EditDepartamentoDto.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "nombre": "Ventas",
                                        "presupuesto": 90000.00
                                    }
                                    """)))
            @Valid @RequestBody EditDepartamentoDto departamento) {

        return ResponseEntity.ok(
                GetDepartamentoDetail.of(departamentoService.edit(id, departamento)));

    }

    @Operation(summary = "Borrar un departamento",
            description = "Borra el departamento. Si no existe no hace nada. No se puede borrar si tiene empleados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Departamento borrado", content = @Content),
            @ApiResponse(responseCode = "409", description = "El departamento tiene empleados",
                    content = @Content(mediaType = "application/problem+json",
                            examples = @ExampleObject(value = """
                                    {
                                        "type": "https://example.com/errors/departamento-con-empleados",
                                        "title": "Departamento con empleados",
                                        "status": 409,
                                        "detail": "No se puede borrar el departamento con id 1 porque tiene empleados asignados",
                                        "instance": "/api/v1/departamentos/1"
                                    }
                                    """)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Id del departamento", example = "4")
            @PathVariable Long id) {
        departamentoService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
