# API REST - Sistema de Registro de Jornada Laboral

![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![OpenAPI](https://img.shields.io/badge/OpenAPI-3.0-6BA539?style=for-the-badge&logo=swagger&logoColor=white)
![License](https://img.shields.io/badge/License-Academic-blue?style=for-the-badge)

Proyecto de desarrollo de una API REST profesional para el control horario y registro de jornada laboral de empleados por departamentos. Desarrollado de forma conjunta para los módulos de **Acceso a Datos (AD)** y **Programación de Servicios y Procesos (PSP)** de 2º de DAM (Curso 2026-27) en Salesianos Triana.

---

## Tabla de Contenidos

1. [Descripción General](#-descripción-general)
2. [Modelo de Datos y Dominio](#-modelo-de-datos-y-dominio)
3. [Decisiones de Diseño y Arquitectura](#-decisiones-de-diseño-y-arquitectura)
   - [Uso de DTOs y Mappings](#uso-de-dtos-y-mappings)
   - [Validaciones y Anotaciones Propias](#validaciones-y-anotaciones-propias)
   - [Internacionalización (i18n)](#internacionalización-i18n)
   - [Gestión Unificada de Errores (ProblemDetail)](#gestión-unificada-de-errores-problemdetail)
4. [Catálogo de Endpoints](#-catálogo-de-endpoints)
5. [Documentación OpenAPI / Swagger](#-documentación-openapi--swagger)
6. [Colección de Postman](#-colección-de-postman)
7. [Instalación y Puesta en Marcha](#-instalación-y-puesta-en-marcha)
8. [Estructura del Proyecto](#-estructura-del-proyecto)
9. [Autor](#-autor)

---

## Descripción General

La aplicación gestiona el control de presencia y fichajes en el entorno corporativo, permitiendo:
- Administrar el ciclo de vida de **Departamentos** y sus presupuestos.
- Gestionar **Empleados**, sus salarios, cargos y la asignación a sus respectivos departamentos.
- Registrar eventos de **Fichaje (Entrada / Salida)** controlando de forma estricta la alternancia de estados y evitando duplicidades.
- Validar las reglas de negocio críticas, como el control presupuestario salarial por departamento.

---

## Modelo de Datos y Dominio

El modelo relacional se compone de tres entidades principales:
- **Departamento:** Representa las unidades de negocio con presupuesto asignado.
- **Empleado:** Personal perteneciente a un departamento.
- **Fichaje:** Registro temporal inmutable con tipo enumerado (`ENTRADA`, `SALIDA`).

---

## 🛠️ Decisiones de Diseño y Arquitectura

### Uso de DTOs y Mappings
Para aislar el modelo de persistencia del contrato de la API y evitar problemas de serialización recursiva o sobreexposición de datos:
- Se han utilizado **Java Records** como objetos de transferencia de datos inmutables para las peticiones (`*RequestDTO`) y respuestas (`*ResponseDTO`).
- Se aplican DTOs anidados donde la respuesta lo requiere (p. ej., `EmpleadoResponseDTO` expone el nombre y datos esenciales del departamento al que pertenece).
- Las transformaciones entre entidad y DTO se realizan mediante mapeadores dedicados garantizando un bajo acoplamiento.

### Validaciones y Anotaciones Propias
1. **Validación estándar de entradas:** Se aplican restricciones como `@NotNull`, `@NotBlank`, `@Positive`, `@Size` en todos los DTOs de entrada.
2. **Anotación de validación personalizada (`@PresupuestoSuficiente`):**
   - Valida a nivel de lógica de negocio que las operaciones de asignación o alta salarial no hagan que el sumatorio de salarios supere el presupuesto límite del departamento.
   - Implementada mediante un validador propio (`ConstraintValidator`) desacoplado.

### Internacionalización (i18n)
- Todos los mensajes devueltos en las validaciones y errores no están hardcodeados en el código.
- Se externalizan completamente en el fichero de recursos `src/main/resources/messages.properties` (y variantes localizadas como `messages_en.properties`), facilitando soporte multidioma y mantenimiento centralizado.

### Gestión Unificada de Errores (ProblemDetail)
Los errores se capturan de forma centralizada con un `@RestControllerAdvice`, traduciendo cada fallo a la especificación estándar **RFC 7807 (`ProblemDetail`)**:
- `EntidadNoEncontradaException` (`DepartamentoNoEncontradoException`, `EmpleadoNoEncontradoException`): Devuelve `404 Not Found`.
- `FichajeDuplicadoException`: Devuelve `409 Conflict` cuando se intenta registrar una `ENTRADA` consecutiva sin una `SALIDA` previa (o viceversa).
- `MethodArgumentNotValidException`: Devuelve `400 Bad Request` desglosando la lista de campos erróneos y sus correspondientes mensajes externalizados.

---

## Catálogo de Endpoints

Todos los endpoints están prefijados bajo la ruta base `/api/v1`.

### Departamentos
| Método | Endpoint | Descripción | Código HTTP |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/v1/departamentos` | Listado paginado/completo de departamentos | `200 OK` |
| `GET` | `/api/v1/departamentos/{id}` | Obtener detalle de un departamento | `200 OK` / `404 Not Found` |
| `POST` | `/api/v1/departamentos` | Crear un nuevo departamento | `201 Created` / `400 Bad Request` |
| `PUT` | `/api/v1/departamentos/{id}` | Actualizar un departamento existente | `200 OK` / `404 Not Found` |
| `DELETE` | `/api/v1/departamentos/{id}` | Eliminar un departamento | `204 No Content` / `404 Not Found` |

### Empleados
| Método | Endpoint | Descripción | Código HTTP |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/v1/empleados` | Listado de empleados | `200 OK` |
| `GET` | `/api/v1/empleados/{id}` | Obtener detalle de un empleado con su DTO anidado | `200 OK` / `404 Not Found` |
| `POST` | `/api/v1/empleados` | Dar de alta un nuevo empleado | `201 Created` / `400 Bad Request` |
| `PUT` | `/api/v1/empleados/{id}` | Actualizar datos del empleado | `200 OK` / `404 Not Found` |
| `PUT` | `/api/v1/empleados/{empleadoId}/departamento/{deptoId}` | Asignar empleado a un departamento | `200 OK` / `404 Not Found` / `400 Bad Request` |
| `DELETE` | `/api/v1/empleados/{id}` | Eliminar un empleado | `204 No Content` / `404 Not Found` |

### Fichajes (Control de Presencia)
| Método | Endpoint | Descripción | Código HTTP |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/v1/empleados/{empleadoId}/fichar` | Registra fichaje alternando automáticamente `ENTRADA` / `SALIDA` | `201 Created` / `409 Conflict` / `404 Not Found` |
| `GET` | `/api/v1/empleados/{empleadoId}/fichajes` | Obtiene el historial de fichajes de un empleado | `200 OK` / `404 Not Found` |

---

## Documentación OpenAPI / Swagger

La API cuenta con documentación interactiva generada con **OpenAPI 3.0**[cite: 3], describiendo esquemas, posibles respuestas de error y ejemplos de prueba en cada endpoint[cite: 3].

Una vez arrancado el servidor en local:
- **Swagger UI:** [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- **Especificación OpenAPI (JSON):** [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

---

## Colección de Postman

En la raíz del proyecto se incluye el fichero exportado:
`postman_collection.json`

La colección contiene:
1. **Peticiones ordenadas por carpetas** (`Departamentos`, `Empleados`, `Fichajes`).
2. **Ejemplos funcionales y casos límite:**
   - Creación y asociación correcta.
   - Prueba de validación de presupuesto superado (`400 Bad Request`).
   - Registro alterno de fichaje y verificación de conflicto por duplicidad (`409 Conflict`).
   - Búsqueda de recursos inexistentes (`404 Not Found`).
3. Variables de entorno configuradas (`{{baseUrl}} = http://localhost:8080/api/v1`).

---

## Instalación y Puesta en Marcha

### Prerrequisitos
- **Java JDK 17** o superior instalado.
- **Maven 3.8+** (o usar el wrapper `./mvnw` incluido).
- Git.

### Pasos para ejecutar
1. Clonar el repositorio:
   ```bash
   git clone https://github.com/Maanuel77/Proyecto-Api-Rest-Registro-De-Jornada.git
   