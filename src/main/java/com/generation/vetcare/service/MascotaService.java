package com.generation.vetcare.service;

import com.generation.vetcare.dto.DuenoResumenDTO;
import com.generation.vetcare.dto.MascotaRequestDTO;
import com.generation.vetcare.dto.MascotaResponseDTO;
import com.generation.vetcare.model.Dueno;
import com.generation.vetcare.model.Mascota;
import com.generation.vetcare.repository.DuenoRepository;
import com.generation.vetcare.repository.MascotaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MascotaService {

    private final MascotaRepository mascotaRepository;
    private final DuenoRepository duenoRepository;

    public MascotaService(MascotaRepository mascotaRepository, DuenoRepository duenoRepository) {
        this.mascotaRepository = mascotaRepository;
        this.duenoRepository = duenoRepository;
    }

    @Transactional(readOnly = true)
    public List<MascotaResponseDTO> listarMascotas() {
        return mascotaRepository.findAll()
                .stream()
                .map(this::mapearAMascotaResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<MascotaResponseDTO> buscarPorId(Long id) {
        return mascotaRepository.findById(id)
                .map(this::mapearAMascotaResponseDTO);
    }

    @Transactional
    public Optional<MascotaResponseDTO> crearMascota(MascotaRequestDTO datos) {
        return duenoRepository.findById(datos.duenoId())
                .map(dueno -> {
                    Mascota mascota = new Mascota();
                    mascota.setNombre(datos.nombre());
                    mascota.setEspecie(datos.especie());
                    mascota.setRaza(datos.raza());
                    mascota.setEdad(datos.edad());
                    mascota.setDueno(dueno);

                    Mascota creada = mascotaRepository.save(mascota);
                    return mapearAMascotaResponseDTO(creada);
                });
    }

    @Transactional
    public Optional<MascotaResponseDTO> actualizarMascota(Long id, MascotaRequestDTO datos) {
        return mascotaRepository.findById(id)
                .flatMap(mascota -> duenoRepository.findById(datos.duenoId())
                        .map(dueno -> {
                            mascota.setNombre(datos.nombre());
                            mascota.setEspecie(datos.especie());
                            mascota.setRaza(datos.raza());
                            mascota.setEdad(datos.edad());
                            mascota.setDueno(dueno);

                            Mascota actualizada = mascotaRepository.save(mascota);
                            return mapearAMascotaResponseDTO(actualizada);
                        }));
    }

    public boolean eliminarMascota(Long id) {
        if (!mascotaRepository.existsById(id)) {
            return false;
        }
        mascotaRepository.deleteById(id);
        return true;
    }

    private MascotaResponseDTO mapearAMascotaResponseDTO(Mascota mascota) {
        Dueno dueno = mascota.getDueno();
        DuenoResumenDTO duenoResumen = new DuenoResumenDTO(dueno.getId(), dueno.getNombre());

        return new MascotaResponseDTO(
                mascota.getId(),
                mascota.getNombre(),
                mascota.getEspecie(),
                mascota.getRaza(),
                mascota.getEdad(),
                duenoResumen
        );
    }
}