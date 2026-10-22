package com.salesianos.dam.RegistroDeJornada.service;

import com.salesianos.dam.RegistroDeJornada.dto.EditFichajeDto;
import com.salesianos.dam.RegistroDeJornada.error.EmpleadoNotFoundException;
import com.salesianos.dam.RegistroDeJornada.error.FichajeDuplicadoException;
import com.salesianos.dam.RegistroDeJornada.model.Empleado;
import com.salesianos.dam.RegistroDeJornada.model.Fichaje;
import com.salesianos.dam.RegistroDeJornada.model.TipoFichaje;
import com.salesianos.dam.RegistroDeJornada.repo.EmpleadoRepository;
import com.salesianos.dam.RegistroDeJornada.repo.FichajeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FichajeService {

    private final FichajeRepository fichajeRepository;
    private final EmpleadoRepository empleadoRepository;

    public Fichaje fichar(Long empleadoId, EditFichajeDto editFichajeDto) {
        Empleado empleado = empleadoRepository.findById(empleadoId)
                .orElseThrow(() -> new EmpleadoNotFoundException(empleadoId));

        TipoFichaje siguiente = fichajeRepository.findFirstByEmpleadoIdOrderByMomentoDesc(empleadoId)
                .map(f -> f.getTipo() == TipoFichaje.ENTRADA ? TipoFichaje.SALIDA : TipoFichaje.ENTRADA)
                .orElse(TipoFichaje.ENTRADA);

        if (editFichajeDto.tipo() != siguiente) {
            throw new FichajeDuplicadoException(editFichajeDto.tipo());
        }

        return fichajeRepository.save(Fichaje.builder()
                .empleado(empleado)
                .tipo(siguiente)
                .momento(LocalDateTime.now())
                .build());
    }

}
