# API REST - Sistema de Registro de Jornada Laboral

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.1-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![OpenAPI](https://img.shields.io/badge/OpenAPI-3.1-6BA539?style=for-the-badge&logo=swagger&logoColor=white)
![License](https://img.shields.io/badge/License-Academic-blue?style=for-the-badge)

Proyecto de desarrollo de una API REST para el control horario y registro de jornada laboral de empleados por departamentos. Desarrollado de forma conjunta para los módulos de **Acceso a Datos (AD)** y **Programación de Servicios y Procesos (PSP)** de 2º de DAM (Curso 2026-27) en Salesianos Triana.

---

## Tabla de Contenidos

1. [Descripción General](#descripción-general)
2. [Modelo de Datos y Dominio](#modelo-de-datos-y-dominio)
3. [Decisiones de Diseño y Arquitectura](#decisiones-de-diseño-y-arquitectura)
   - [Uso de DTOs](#uso-de-dtos)
   - [Validaciones](#validaciones)
   - [Internacionalización (i18n)](#internacionalización-i18n)
   - [Gestión Unificada de Errores (ProblemDetail)](#gestión-unificada-de-errores-problemdetail)
4. [Catálogo de Endpoints](#catálogo-de-endpoints)
5. [Documentación OpenAPI / Swagger](#documentación-openapi--swagger)
6. [Colección de Postman](#colección-de-postman)
7. [Instalación y Puesta en Marcha](#instalación-y-puesta-en-marcha)
8. [Estructura del Proyecto](#estructura-del-proyecto)
9. [Autor](#autor)

---

## Descripción General

La aplicación gestiona el control de presencia y fichajes en el entorno corporativo, permitiendo:
- Administrar el ciclo de vida de **Departamentos** y sus presupuestos.
- Gestionar **Empleados**, sus salarios, cargos y la asignación a sus respectivos departamentos.
- Registrar eventos de **Fichaje (Entrada / Salida)** controlando de forma estricta la alternancia de tipos y evitando duplicidades.
- Validar las reglas de negocio críticas, como el control presupuestario salarial por departamento.

---

## Modelo de Datos y Dominio

El modelo se compone de tres entidades JPA y un enumerado:

| Entidad | Atributos | Relaciones |
| :--- | :--- | :--- |
| **Departamento** | `id`, `nombre` (único), `presupuesto` (`BigDecimal`) | `@OneToMany` con Empleado |
| **Empleado** | `id`, `nombreCompleto`, `cargo`, `salario` (`BigDecimal`) | `@ManyToOne` con Departamento, `@OneToMany` con Fichaje |
| **Fichaje** | `id`, `momento` (`LocalDateTime`), `tipo` (`@Enumerated(STRING)`) | `@ManyToOne` con Empleado |
| **TipoFichaje** | `ENTRADA`, `SALIDA` | — |

```
Departamento 1 ──── * Empleado 1 ──── * Fichaje
```

Al arrancar, la clase `InitData` carga datos de ejemplo: 3 departamentos, 4 empleados y 6 fichajes.

---

## Decisiones de Diseño y Arquitectura

La aplicación sigue una arquitectura por capas: `controller` → `service` → `repo` → `model`, con los paquetes `dto`, `error` y `config` como apoyo.

### Uso de DTOs
Para aislar el modelo de persistencia del contrato de la API y evitar problemas de serialización recursiva o sobreexposición de datos se usan **Java Records**:
- **Entrada:** `EditDepartamentoDto`, `EditEmpleadoDto`, `EditFichajeDto`. Incluyen un método `to()` que construye la entidad.
- **Salida:** `GetDepartamentoList`, `GetDepartamentoDetail`, `GetEmpleadoList`, `GetEmpleadoDetail`, `GetEmpleadoResumen` y `GetFichajeDto`. Incluyen un método estático `of()` que convierte la entidad en DTO.
- Se usan DTOs anidados cuando la respuesta lo requiere: `GetDepartamentoDetail` incluye la lista de empleados como `GetEmpleadoResumen`, y `GetEmpleadoDetail` expone el id y el nombre de su departamento.

### Validaciones
1. **Validación estándar de entradas** con `@Valid` en todos los `@RequestBody`:
   - Textos: `@NotBlank` y `@Size`.
   - Importes: `@NotNull`, `@Positive` y `@Digits(integer = 10, fraction = 2)`.
2. **Anotación de validación personalizada `@PresupuestoSuficiente`** (paquete `validation`):
   - Se aplica a nivel de clase sobre `EditEmpleadoDto`, por lo que se comprueba al crear y al editar un empleado.
   - Su validador `PresupuestoSuficienteValidator` (`ConstraintValidator`) suma los salarios del departamento más el nuevo salario y comprueba que no superen el presupuesto. Al editar, obtiene el id del empleado de la URL para no contar dos veces su salario.
   - Si no se cumple, la petición se rechaza con `400 Bad Request` y el mensaje externalizado `empleado.presupuesto.excedido`.
   - En las operaciones sin body (asignar un empleado a un departamento) y al reducir el presupuesto de un departamento, la misma regla se comprueba en el service y lanza `PresupuestoExcedidoException` (`400 Bad Request`).
3. **Reglas de negocio adicionales:**
   - El nombre de un departamento es único (`409 Conflict`).
   - No se puede borrar un departamento con empleados ni un empleado con fichajes (`409 Conflict`).
   - Los fichajes deben alternar `ENTRADA` y `SALIDA` (`409 Conflict`).

### Internacionalización (i18n)
- Ningún mensaje de error ni de validación está escrito en el código: se externalizan en `src/main/resources/language/messages_es.properties` y `messages_en.properties`.
- La clase `config/WebConfig` define el `MessageSource`, un `SessionLocaleResolver` con español como idioma por defecto y un `LocaleChangeInterceptor`.
- El idioma se cambia con el parámetro `lang`, por ejemplo `GET /api/v1/departamentos/99?lang=en`.

### Gestión Unificada de Errores (ProblemDetail)
Los errores se capturan de forma centralizada en `GlobalExceptionHandler` (`@RestControllerAdvice`), que devuelve un `ProblemDetail` (RFC 7807) con `title`, `detail` y `type`:

| Excepción | Código |
| :--- | :--- |
| `DepartamentoNotFoundException`, `EmpleadoNotFoundException`, `FichajeNotFoundException` | `404 Not Found` |
| `PresupuestoExcedidoException` | `400 Bad Request` |
| `DepartamentoDuplicadoException` | `409 Conflict` |
| `DepartamentoConEmpleadosException`, `EmpleadoConFichajesException` | `409 Conflict` |
| `FichajeDuplicadoException` | `409 Conflict` |
| Errores de validación (`@Valid`) | `400 Bad Request` |

Ejemplo de respuesta de error:

```json
{
  "type": "https://example.com/errors/departamento-not-found",
  "title": "Recurso no encontrado",
  "status": 404,
  "detail": "No se ha encontrado el departamento con id 99",
  "instance": "/api/v1/departamentos/99"
}
```

---

## Catálogo de Endpoints

Todos los endpoints están bajo la ruta base `/api/v1`.

### Departamentos
| Método | Endpoint | Descripción | Códigos HTTP |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/v1/departamentos` | Listado de departamentos con su número de empleados | `200` / `404` |
| `GET` | `/api/v1/departamentos/{id}` | Detalle de un departamento con sus empleados | `200` / `404` |
| `POST` | `/api/v1/departamentos` | Crear un departamento | `201` / `400` / `409` |
| `PUT` | `/api/v1/departamentos/{id}` | Editar un departamento | `200` / `400` / `404` / `409` |
| `DELETE` | `/api/v1/departamentos/{id}` | Borrar un departamento (idempotente) | `204` / `409` |

### Empleados
| Método | Endpoint | Descripción | Códigos HTTP |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/v1/empleados` | Listado de empleados | `200` / `404` |
| `GET` | `/api/v1/empleados/{id}` | Detalle de un empleado con su departamento | `200` / `404` |
| `POST` | `/api/v1/empleados` | Dar de alta un empleado | `201` / `400` / `404` |
| `PUT` | `/api/v1/empleados/{id}` | Editar un empleado | `200` / `400` / `404` |
| `PUT` | `/api/v1/empleados/{empleadoId}/departamento/{deptoId}` | Asignar un empleado a un departamento | `200` / `400` / `404` |
| `DELETE` | `/api/v1/empleados/{id}` | Borrar un empleado (idempotente) | `204` / `409` |

### Fichajes
| Método | Endpoint | Descripción | Códigos HTTP |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/v1/empleados/{empleadoId}/fichar` | Registra un fichaje con la hora actual. Body: `{"tipo": "ENTRADA"}` | `201` / `400` / `404` / `409` |
| `GET` | `/api/v1/empleados/{empleadoId}/fichajes` | Historial de fichajes del más reciente al más antiguo | `200` / `404` |

---

## Documentación OpenAPI / Swagger

La API cuenta con documentación interactiva generada con **springdoc-openapi** (OpenAPI 3.1). Cada controlador tiene su `@Tag` y cada endpoint su `@Operation`, `@ApiResponses`, `@Parameter` y ejemplos JSON de petición y respuesta.

Una vez arrancado el servidor en local:
- **Swagger UI:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **Especificación OpenAPI (JSON):** [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

---

## Colección de Postman

La colección está en [`postman/Registro de Jornada.postman_collection.json`](postman/Registro%20de%20Jornada.postman_collection.json).

- **Carpetas:** `Departamentos`, `Empleados`, `Asignación`, `Fichajes` y `Errores`.
- **Variable** `{{baseUrl}}` = `http://localhost:8080`.
- **Casos correctos y de error** (`400`, `404` y `409`), cada uno con un test que comprueba el código de respuesta.

Para usarla: en Postman, **Import** → seleccionar el fichero → **Run collection**. Debe ejecutarse en orden con la aplicación recién arrancada, ya que parte de los datos de `InitData`.

---

## Instalación y Puesta en Marcha

### Prerrequisitos
- **Java JDK 21**.
- Git.
- No hace falta instalar Maven: se incluye el wrapper `mvnw`.

### Pasos para ejecutar
1. Clonar el repositorio:
   ```bash
   git clone https://github.com/Maanuel77/Proyecto-Api-Rest-Registro-De-Jornada.git
   cd Proyecto-Api-Rest-Registro-De-Jornada/RegistroDeJornada
   ```
2. Arrancar la aplicación:
   ```bash
   ./mvnw spring-boot:run      # Linux / macOS
   .\mvnw spring-boot:run      # Windows
   ```
3. La API queda disponible en `http://localhost:8080`.

### Base de datos
Se usa **H2** en fichero (`./db/basedatos`) con `ddl-auto=create-drop`, por lo que la base se recrea en cada arranque.
- **Consola H2:** [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
- **JDBC URL:** `jdbc:h2:./db/basedatos` · **Usuario:** `sa` · **Contraseña:** vacía

---

## Estructura del Proyecto

```
RegistroDeJornada/
├── pom.xml
└── src/main/
    ├── java/com/salesianos/dam/RegistroDeJornada/
    │   ├── config/        # WebConfig (i18n)
    │   ├── controller/    # Departamento, Empleado y Fichaje
    │   ├── dto/           # Records de entrada y salida
    │   ├── error/         # Excepciones propias y GlobalExceptionHandler
    │   ├── model/         # Entidades JPA y TipoFichaje
    │   ├── repo/          # Repositorios JPA
    │   ├── service/       # Lógica de negocio
    │   ├── validation/    # @PresupuestoSuficiente y su validador
    │   ├── InitData.java  # Datos de ejemplo
    │   └── RegistroDeJornadaApplication.java
    └── resources/
        ├── application.properties
        └── language/      # messages_es / messages_en
postman/
└── Registro de Jornada.postman_collection.json
```

---

## Autor

**Manuel Martínez** — [@Maanuel77](https://github.com/Maanuel77)
