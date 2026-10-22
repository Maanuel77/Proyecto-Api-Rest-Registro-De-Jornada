package com.salesianos.dam.RegistroDeJornada.repo;

import com.salesianos.dam.RegistroDeJornada.model.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DepartamentoRepository
    extends JpaRepository<Departamento, Long> {

    boolean existsByNombre(String nombre);

    boolean existsByNombreAndIdNot(String nombre, Long id);

}
