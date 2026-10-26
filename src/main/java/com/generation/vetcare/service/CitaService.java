package com.generation.vetcare.service;

import com.generation.vetcare.dto.CitaRequestDTO;
import com.generation.vetcare.dto.CitaResponseDTO;
import com.generation.vetcare.dto.MascotaResumenDTO;
import com.generation.vetcare.dto.VeterinarioResumenDTO;
import com.generation.vetcare.exception.ResourceNotFoundException;
import com.generation.vetcare.model.Cita;
import com.generation.vetcare.model.EstadoCita;
import com.generation.vetcare.model.Mascota;
import com.generation.vetcare.model.Veterinario;
import com.generation.vetcare.repository.CitaRepository;
import com.generation.vetcare.repository.MascotaRepository;
import com.generation.vetcare.repository.VeterinarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CitaService {

    private final CitaRepository citaRepository;
    private final MascotaRepository mascotaRepository;
    private final VeterinarioRepository veterinarioRepository;

    public CitaService(CitaRepository citaRepository, MascotaRepository mascotaRepository, VeterinarioRepository veterinarioRepository) {
        this.citaRepository = citaRepository;
        this.mascotaRepository = mascotaRepository;
        this.veterinarioRepository = veterinarioRepository;
    }

    @Transactional(readOnly = true)
    public List<CitaResponseDTO> listarCitas() {
        return citaRepository.findAll()
                .stream()
                .map(this::mapearACitaResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public CitaResponseDTO buscarPorId(Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe una cita con id " + id));
        return mapearACitaResponseDTO(cita);
    }

    @Transactional
    public Optional<CitaResponseDTO> crearCita(CitaRequestDTO datos) {
        Optional<Mascota> mascota = mascotaRepository.findById(datos.mascotaId());
        Optional<Veterinario> veterinario = veterinarioRepository.findById(datos.veterinarioId());

        if (mascota.isEmpty() || veterinario.isEmpty()) {
            return Optional.empty();
        }

        Cita cita = new Cita();
        cita.setFechaHora(datos.fechaHora());
        cita.setMotivo(datos.motivo());
        cita.setEstado(EstadoCita.PENDIENTE);
        cita.setMascota(mascota.get());
        cita.setVeterinario(veterinario.get());

        Cita creada = citaRepository.save(cita);
        return Optional.of(mapearACitaResponseDTO(creada));
    }

    @Transactional
    public CitaResponseDTO marcarComoAtendida(Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe una cita con id " + id));

        cita.setEstado(EstadoCita.ATENDIDA);
        Cita actualizada = citaRepository.save(cita);
        return mapearACitaResponseDTO(actualizada);
    }

    public void eliminarCita(Long id) {
        if (!citaRepository.existsById(id)) {
            throw new ResourceNotFoundException("No existe una cita con id " + id);
        }
        citaRepository.deleteById(id);
    }

    private CitaResponseDTO mapearACitaResponseDTO(Cita cita) {
        MascotaResumenDTO mascotaResumen = new MascotaResumenDTO(
                cita.getMascota().getId(),
                cita.getMascota().getNombre(),
                cita.getMascota().getEspecie()
        );
        VeterinarioResumenDTO veterinarioResumen = new VeterinarioResumenDTO(
                cita.getVeterinario().getId(),
                cita.getVeterinario().getNombre()
        );

        return new CitaResponseDTO(
                cita.getId(),
                cita.getFechaHora(),
                cita.getMotivo(),
                cita.getEstado(),
                mascotaResumen,
                veterinarioResumen
        );
    }
}