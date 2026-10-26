package com.generation.vetcare.service;

import com.generation.vetcare.dto.EspecialidadResponseDTO;
import com.generation.vetcare.dto.VeterinarioRequestDTO;
import com.generation.vetcare.dto.VeterinarioResponseDTO;
import com.generation.vetcare.exception.ResourceNotFoundException;
import com.generation.vetcare.model.Especialidad;
import com.generation.vetcare.model.Veterinario;
import com.generation.vetcare.repository.EspecialidadRepository;
import com.generation.vetcare.repository.VeterinarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VeterinarioService {

    private final VeterinarioRepository veterinarioRepository;
    private final EspecialidadRepository especialidadRepository;

    public VeterinarioService(VeterinarioRepository veterinarioRepository, EspecialidadRepository especialidadRepository) {
        this.veterinarioRepository = veterinarioRepository;
        this.especialidadRepository = especialidadRepository;
    }

    @Transactional(readOnly = true)
    public List<VeterinarioResponseDTO> listarVeterinarios() {
        return veterinarioRepository.findAll()
                .stream()
                .map(this::mapearAVeterinarioResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public VeterinarioResponseDTO buscarPorId(Long id) {
        Veterinario veterinario = veterinarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe un veterinario con id " + id));
        return mapearAVeterinarioResponseDTO(veterinario);
    }

    @Transactional
    public VeterinarioResponseDTO crearVeterinario(VeterinarioRequestDTO datos) {
        List<Especialidad> especialidades = especialidadRepository.findAllById(datos.especialidadIds());

        Veterinario veterinario = new Veterinario();
        veterinario.setNombre(datos.nombre());
        veterinario.setTelefono(datos.telefono());
        veterinario.setEmail(datos.email());
        veterinario.setEspecialidades(especialidades);

        Veterinario creado = veterinarioRepository.save(veterinario);
        return mapearAVeterinarioResponseDTO(creado);
    }

    public void eliminarVeterinario(Long id) {
        if (!veterinarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("No existe un veterinario con id " + id);
        }
        veterinarioRepository.deleteById(id);
    }

    private VeterinarioResponseDTO mapearAVeterinarioResponseDTO(Veterinario veterinario) {
        List<EspecialidadResponseDTO> especialidadesDTO = veterinario.getEspecialidades()
                .stream()
                .map(especialidad -> new EspecialidadResponseDTO(especialidad.getId(), especialidad.getNombre()))
                .toList();

        return new VeterinarioResponseDTO(
                veterinario.getId(),
                veterinario.getNombre(),
                veterinario.getTelefono(),
                veterinario.getEmail(),
                especialidadesDTO
        );
    }
}