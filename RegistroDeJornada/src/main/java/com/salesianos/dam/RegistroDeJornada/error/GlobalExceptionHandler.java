package com.salesianos.dam.RegistroDeJornada.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(DepartamentoNotFoundException.class)
    public ProblemDetail handleDepartamentoNotFound(DepartamentoNotFoundException ex) {

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problem.setTitle("Recurso no encontrado");
        problem.setDetail(ex.getMessage());
        problem.setType(URI.create("https://example.com/errors/departamento-not-found"));

        return problem;
    }

    @ExceptionHandler(DepartamentoConEmpleadosException.class)
    public ProblemDetail handleDepartamentoConEmpleados(DepartamentoConEmpleadosException ex) {

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problem.setTitle("Departamento con empleados");
        problem.setDetail(ex.getMessage());
        problem.setType(URI.create("https://example.com/errors/departamento-con-empleados"));

        return problem;
    }


}
