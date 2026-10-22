package com.salesianos.dam.RegistroDeJornada.error;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;


    @ExceptionHandler(DepartamentoNotFoundException.class)
    public ProblemDetail handleDepartamentoNotFound(DepartamentoNotFoundException ex) {

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problem.setTitle(getMessage("error.notfound.title"));
        problem.setDetail(getMessage(ex.getMessage(), ex.getArgs()));
        problem.setType(URI.create("https://example.com/errors/departamento-not-found"));

        return problem;
    }

    @ExceptionHandler(DepartamentoConEmpleadosException.class)
    public ProblemDetail handleDepartamentoConEmpleados(DepartamentoConEmpleadosException ex) {

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problem.setTitle(getMessage("error.departamento.conempleados.title"));
        problem.setDetail(getMessage(ex.getMessage(), ex.getArgs()));
        problem.setType(URI.create("https://example.com/errors/departamento-con-empleados"));

        return problem;
    }

    @ExceptionHandler(EmpleadoNotFoundException.class)
    public ProblemDetail handleEmpleadoNotFound(EmpleadoNotFoundException ex) {

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problem.setTitle(getMessage("error.notfound.title"));
        problem.setDetail(getMessage(ex.getMessage(), ex.getArgs()));
        problem.setType(URI.create("https://example.com/errors/empleado-not-found"));

        return problem;
    }

    @ExceptionHandler(EmpleadoConFichajesException.class)
    public ProblemDetail handleEmpleadoConFichajes(EmpleadoConFichajesException ex) {

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problem.setTitle(getMessage("error.empleado.confichajes.title"));
        problem.setDetail(getMessage(ex.getMessage(), ex.getArgs()));
        problem.setType(URI.create("https://example.com/errors/empleado-con-fichajes"));

        return problem;
    }

    @ExceptionHandler(FichajeDuplicadoException.class)
    public ProblemDetail handleFichajeDuplicado(FichajeDuplicadoException ex) {

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problem.setTitle(getMessage("error.fichaje.duplicado.title"));
        problem.setDetail(getMessage(ex.getMessage(), ex.getArgs()));
        problem.setType(URI.create("https://example.com/errors/fichaje-duplicado"));

        return problem;
    }

    @ExceptionHandler(FichajeNotFoundException.class)
    public ProblemDetail handleFichajeNotFound(FichajeNotFoundException ex) {

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problem.setTitle(getMessage("error.notfound.title"));
        problem.setDetail(getMessage(ex.getMessage(), ex.getArgs()));
        problem.setType(URI.create("https://example.com/errors/fichaje-not-found"));

        return problem;
    }

    @ExceptionHandler(DepartamentoDuplicadoException.class)
    public ProblemDetail handleDepartamentoDuplicado(DepartamentoDuplicadoException ex) {

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problem.setTitle(getMessage("error.departamento.duplicado.title"));
        problem.setDetail(getMessage(ex.getMessage(), ex.getArgs()));
        problem.setType(URI.create("https://example.com/errors/departamento-duplicado"));

        return problem;
    }

    @ExceptionHandler(PresupuestoExcedidoException.class)
    public ProblemDetail handlePresupuestoExcedido(PresupuestoExcedidoException ex) {

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle(getMessage("error.presupuesto.excedido.title"));
        problem.setDetail(getMessage(ex.getMessage(), ex.getArgs()));
        problem.setType(URI.create("https://example.com/errors/presupuesto-excedido"));

        return problem;
    }

    private String getMessage(String key, Object... args) {
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
    }

}
