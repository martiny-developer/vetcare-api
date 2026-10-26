package com.generation.vetcare.dto;

import java.util.List;

public record VeterinarioResponseDTO(
        Long id,
        String nombre,
        String telefono,
        String email,
        List<EspecialidadResponseDTO> especialidades
) {
}