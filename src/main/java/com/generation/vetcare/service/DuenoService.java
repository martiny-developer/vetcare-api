package com.generation.vetcare.service;

import com.generation.vetcare.dto.DuenoRequestDTO;
import com.generation.vetcare.dto.DuenoResponseDTO;
import com.generation.vetcare.dto.MascotaResumenDTO;
import com.generation.vetcare.model.Dueno;
import com.generation.vetcare.repository.DuenoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DuenoService {

    private final DuenoRepository duenoRepository;

    public DuenoService(DuenoRepository duenoRepository) {
        this.duenoRepository = duenoRepository;
    }

    @Transactional(readOnly = true)
    public List<DuenoResponseDTO> listarDuenos() {
        return duenoRepository.findAll()
                .stream()
                .map(this::mapearADuenoResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<DuenoResponseDTO> buscarPorId(Long id) {
        return duenoRepository.findById(id)
                .map(this::mapearADuenoResponseDTO);
    }

    @Transactional
    public DuenoResponseDTO crearDueno(DuenoRequestDTO datos) {
        Dueno dueno = new Dueno();
        dueno.setNombre(datos.nombre());
        dueno.setTelefono(datos.telefono());
        dueno.setDireccion(datos.direccion());

        Dueno creado = duenoRepository.save(dueno);
        return mapearADuenoResponseDTO(creado);
    }

    @Transactional
    public Optional<DuenoResponseDTO> actualizarDueno(Long id, DuenoRequestDTO datos) {
        return duenoRepository.findById(id)
                .map(dueno -> {
                    dueno.setNombre(datos.nombre());
                    dueno.setTelefono(datos.telefono());
                    dueno.setDireccion(datos.direccion());

                    Dueno actualizado = duenoRepository.save(dueno);
                    return mapearADuenoResponseDTO(actualizado);
                });
    }

    public boolean eliminarDueno(Long id) {
        if (!duenoRepository.existsById(id)) {
            return false;
        }
        duenoRepository.deleteById(id);
        return true;
    }

    private DuenoResponseDTO mapearADuenoResponseDTO(Dueno dueno) {
        List<MascotaResumenDTO> mascotasResumen = dueno.getMascotas()
                .stream()
                .map(mascota -> new MascotaResumenDTO(
                        mascota.getId(),
                        mascota.getNombre(),
                        mascota.getEspecie()
                ))
                .toList();

        return new DuenoResponseDTO(
                dueno.getId(),
                dueno.getNombre(),
                dueno.getTelefono(),
                dueno.getDireccion(),
                mascotasResumen
        );
    }
}