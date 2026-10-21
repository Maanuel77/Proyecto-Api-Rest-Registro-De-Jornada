package com.salesianos.dam.RegistroDeJornada.repo;

import com.salesianos.dam.RegistroDeJornada.model.Fichaje;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface FichajeRepository
    extends JpaRepository<Fichaje, Long> {

    Optional<Fichaje> findFirstByEmpleadoIdOrderByMomentoDesc(Long empleadoId);

    List<Fichaje> findByEmpleadoIdOrderByMomentoDesc(Long empleadoId);

}
