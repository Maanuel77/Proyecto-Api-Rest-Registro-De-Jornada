package com.salesianos.dam.RegistroDeJornada.controller;

import com.salesianos.dam.RegistroDeJornada.dto.EditEmpleadoDto;
import com.salesianos.dam.RegistroDeJornada.dto.GetEmpleadoDetail;
import com.salesianos.dam.RegistroDeJornada.dto.GetEmpleadoList;
import com.salesianos.dam.RegistroDeJornada.model.Empleado;
import com.salesianos.dam.RegistroDeJornada.service.EmpleadoService;
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
@RequestMapping("/api/v1/empleados")
@Tag(name = "Empleados", description = "Gestión de los empleados y su asignación a departamentos")
public class EmpleadoController {

    private final EmpleadoService empleadoService;

    @Operation(summary = "Listar empleados", description = "Devuelve todos los empleados con el nombre de su departamento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empleados encontrados",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GetEmpleadoList.class)),
                            examples = @ExampleObject(value = """
                                    [
                                        {
                                            "id": 1,
                                            "nombreCompleto": "Laura García Pérez",
                                            "cargo": "Técnica de RRHH",
                                            "nombreDepartamento": "Recursos Humanos"
                                        },
                                        {
                                            "id": 2,
                                            "nombreCompleto": "Carlos Ruiz Martín",
                                            "cargo": "Desarrollador Backend",
                                            "nombreDepartamento": "Desarrollo"
                                        }
                                    ]
                                    """))),
            @ApiResponse(responseCode = "404", description = "No hay empleados",
                    content = @Content(mediaType = "application/problem+json",
                            examples = @ExampleObject(value = """
                                    {
                                        "type": "https://example.com/errors/empleado-not-found",
                                        "title": "Recurso no encontrado",
                                        "status": 404,
                                        "detail": "No se han encontrado empleados",
                                        "instance": "/api/v1/empleados"
                                    }
                                    """)))
    })
    @GetMapping
    public ResponseEntity<List<GetEmpleadoList>> getAll() {

        List<Empleado> result = empleadoService.findAll();
        return ResponseEntity.ok(
                result
                        .stream()
                        .map(GetEmpleadoList::of)
                        .toList());
    }

    @Operation(summary = "Obtener un empleado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empleado encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetEmpleadoDetail.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "id": 2,
                                        "nombreCompleto": "Carlos Ruiz Martín",
                                        "cargo": "Desarrollador Backend",
                                        "salario": 35000.00,
                                        "departamentoId": 2,
                                        "nombreDepartamento": "Desarrollo"
                                    }
                                    """))),
            @ApiResponse(responseCode = "404", description = "No existe el empleado",
                    content = @Content(mediaType = "application/problem+json",
                            examples = @ExampleObject(value = """
                                    {
                                        "type": "https://example.com/errors/empleado-not-found",
                                        "title": "Recurso no encontrado",
                                        "status": 404,
                                        "detail": "No se ha encontrado el empleado con id 99",
                                        "instance": "/api/v1/empleados/99"
                                    }
                                    """)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<GetEmpleadoDetail> getById(
            @Parameter(description = "Id del empleado", example = "2")
            @PathVariable Long id) {

        return ResponseEntity.ok(GetEmpleadoDetail.of(empleadoService.findById(id)));

    }

    @Operation(summary = "Crear un empleado",
            description = "Crea el empleado en el departamento indicado. La suma de salarios no puede superar el presupuesto.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Empleado creado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetEmpleadoDetail.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "id": 5,
                                        "nombreCompleto": "Pedro Gil Romero",
                                        "cargo": "QA",
                                        "salario": 30000.00,
                                        "departamentoId": 2,
                                        "nombreDepartamento": "Desarrollo"
                                    }
                                    """))),
            @ApiResponse(responseCode = "400", description = "Datos no válidos o presupuesto excedido",
                    content = @Content(mediaType = "application/problem+json",
                            examples = @ExampleObject(value = """
                                    {
                                        "type": "https://example.com/errors/presupuesto-excedido",
                                        "title": "Presupuesto excedido",
                                        "status": 400,
                                        "detail": "La suma de salarios (58.000) supera el presupuesto del departamento Recursos Humanos (50.000)",
                                        "instance": "/api/v1/empleados"
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
                                        "instance": "/api/v1/empleados"
                                    }
                                    """)))
    })
    @PostMapping
    public ResponseEntity<GetEmpleadoDetail> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del empleado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EditEmpleadoDto.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "nombreCompleto": "Pedro Gil Romero",
                                        "cargo": "QA",
                                        "salario": 30000.00,
                                        "departamentoId": 2
                                    }
                                    """)))
            @Valid @RequestBody EditEmpleadoDto empleado) {

        return ResponseEntity.status(201)
                .body(GetEmpleadoDetail.of(empleadoService.create(empleado)));

    }

    @Operation(summary = "Editar un empleado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empleado editado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetEmpleadoDetail.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "id": 1,
                                        "nombreCompleto": "Laura García Pérez",
                                        "cargo": "Responsable de RRHH",
                                        "salario": 32000.00,
                                        "departamentoId": 1,
                                        "nombreDepartamento": "Recursos Humanos"
                                    }
                                    """))),
            @ApiResponse(responseCode = "400", description = "Datos no válidos o presupuesto excedido", content = @Content),
            @ApiResponse(responseCode = "404", description = "No existe el empleado o el departamento",
                    content = @Content(mediaType = "application/problem+json",
                            examples = @ExampleObject(value = """
                                    {
                                        "type": "https://example.com/errors/empleado-not-found",
                                        "title": "Recurso no encontrado",
                                        "status": 404,
                                        "detail": "No se ha encontrado el empleado con id 99",
                                        "instance": "/api/v1/empleados/99"
                                    }
                                    """)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<GetEmpleadoDetail> edit(
            @Parameter(description = "Id del empleado", example = "1")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Nuevos datos del empleado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EditEmpleadoDto.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "nombreCompleto": "Laura García Pérez",
                                        "cargo": "Responsable de RRHH",
                                        "salario": 32000.00,
                                        "departamentoId": 1
                                    }
                                    """)))
            @Valid @RequestBody EditEmpleadoDto empleado) {

        return ResponseEntity.ok(
                GetEmpleadoDetail.of(empleadoService.edit(id, empleado)));

    }

    @Operation(summary = "Asignar un empleado a un departamento",
            description = "Cambia el departamento del empleado. La suma de salarios no puede superar el presupuesto.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empleado asignado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetEmpleadoDetail.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "id": 1,
                                        "nombreCompleto": "Laura García Pérez",
                                        "cargo": "Técnica de RRHH",
                                        "salario": 28000.00,
                                        "departamentoId": 3,
                                        "nombreDepartamento": "Ventas"
                                    }
                                    """))),
            @ApiResponse(responseCode = "400", description = "Presupuesto excedido",
                    content = @Content(mediaType = "application/problem+json",
                            examples = @ExampleObject(value = """
                                    {
                                        "type": "https://example.com/errors/presupuesto-excedido",
                                        "title": "Presupuesto excedido",
                                        "status": 400,
                                        "detail": "La suma de salarios (63.000) supera el presupuesto del departamento Recursos Humanos (50.000)",
                                        "instance": "/api/v1/empleados/2/departamento/1"
                                    }
                                    """))),
            @ApiResponse(responseCode = "404", description = "No existe el empleado o el departamento",
                    content = @Content(mediaType = "application/problem+json",
                            examples = @ExampleObject(value = """
                                    {
                                        "type": "https://example.com/errors/departamento-not-found",
                                        "title": "Recurso no encontrado",
                                        "status": 404,
                                        "detail": "No se ha encontrado el departamento con id 99",
                                        "instance": "/api/v1/empleados/1/departamento/99"
                                    }
                                    """)))
    })
    @PutMapping("/{empleadoId}/departamento/{deptoId}")
    public ResponseEntity<GetEmpleadoDetail> asignarDepartamento(
            @Parameter(description = "Id del empleado", example = "1")
            @PathVariable Long empleadoId,
            @Parameter(description = "Id del departamento", example = "3")
            @PathVariable Long deptoId) {

        return ResponseEntity.ok(
                GetEmpleadoDetail.of(empleadoService.asignarDepartamento(empleadoId, deptoId)));

    }

    @Operation(summary = "Borrar un empleado",
            description = "Borra el empleado. Si no existe no hace nada. No se puede borrar si tiene fichajes.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Empleado borrado", content = @Content),
            @ApiResponse(responseCode = "409", description = "El empleado tiene fichajes",
                    content = @Content(mediaType = "application/problem+json",
                            examples = @ExampleObject(value = """
                                    {
                                        "type": "https://example.com/errors/empleado-con-fichajes",
                                        "title": "Empleado con fichajes",
                                        "status": 409,
                                        "detail": "No se puede borrar el empleado con id 1 porque tiene fichajes registrados",
                                        "instance": "/api/v1/empleados/1"
                                    }
                                    """)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Id del empleado", example = "4")
            @PathVariable Long id) {
        empleadoService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
