package com.salesianos.dam.RegistroDeJornada.repo;

import com.salesianos.dam.RegistroDeJornada.model.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EmpleadoRepository
    extends JpaRepository<Empleado, Long> {}
