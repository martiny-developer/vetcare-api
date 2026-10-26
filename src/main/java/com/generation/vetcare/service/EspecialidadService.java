package com.generation.vetcare.service;

import com.generation.vetcare.dto.EspecialidadRequestDTO;
import com.generation.vetcare.dto.EspecialidadResponseDTO;
import com.generation.vetcare.exception.ConflictException;
import com.generation.vetcare.exception.ResourceNotFoundException;
import com.generation.vetcare.model.Especialidad;
import com.generation.vetcare.repository.EspecialidadRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EspecialidadService {

    private final EspecialidadRepository especialidadRepository;

    public EspecialidadService(EspecialidadRepository especialidadRepository) {
        this.especialidadRepository = especialidadRepository;
    }

    @Transactional(readOnly = true)
    public List<EspecialidadResponseDTO> listarEspecialidades() {
        return especialidadRepository.findAll(Sort.by("nombre"))
                .stream()
                .map(this::mapearAEspecialidadResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public EspecialidadResponseDTO buscarPorId(Long id) {
        return mapearAEspecialidadResponseDTO(obtenerPorId(id));
    }

    @Transactional
    public EspecialidadResponseDTO crearEspecialidad(EspecialidadRequestDTO datos) {
        String nombre = datos.nombre().trim();

        if (especialidadRepository.existsByNombreIgnoreCase(nombre)) {
            throw new ConflictException("Ya existe una especialidad llamada " + nombre);
        }

        Especialidad especialidad = new Especialidad();
        especialidad.setNombre(nombre);
        return mapearAEspecialidadResponseDTO(especialidadRepository.save(especialidad));
    }

    @Transactional
    public EspecialidadResponseDTO actualizarEspecialidad(Long id, EspecialidadRequestDTO datos) {
        Especialidad especialidad = obtenerPorId(id);
        String nombre = datos.nombre().trim();

        boolean cambioNombre = !especialidad.getNombre().equalsIgnoreCase(nombre);
        if (cambioNombre && especialidadRepository.existsByNombreIgnoreCase(nombre)) {
            throw new ConflictException("Ya existe una especialidad llamada " + nombre);
        }

        especialidad.setNombre(nombre);
        return mapearAEspecialidadResponseDTO(especialidad);
    }

    @Transactional
    public void eliminarEspecialidad(Long id) {
        if (!especialidadRepository.existsById(id)) {
            throw new ResourceNotFoundException("No existe una especialidad con id " + id);
        }
        especialidadRepository.deleteById(id);
    }

    private Especialidad obtenerPorId(Long id) {
        return especialidadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe una especialidad con id " + id));
    }

    private EspecialidadResponseDTO mapearAEspecialidadResponseDTO(Especialidad especialidad) {
        return new EspecialidadResponseDTO(especialidad.getId(), especialidad.getNombre());
    }
}