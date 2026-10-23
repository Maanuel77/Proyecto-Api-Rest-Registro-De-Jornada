package com.salesianos.dam.RegistroDeJornada;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(
		title = "API Registro de Jornada",
		description = "API REST para gestionar departamentos, empleados y el registro de fichajes de entrada y salida.",
		version = "1.0",
		contact = @Contact(name = "Manuel Martínez", url = "https://github.com/Maanuel77")
))
public class RegistroDeJornadaApplication {

	public static void main(String[] args) {
		SpringApplication.run(RegistroDeJornadaApplication.class, args);
	}

}
