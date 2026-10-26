package com.generation.vetcare.dto;

import java.util.List;

public record DuenoResponseDTO(
        Long id,
        String nombre,
        String telefono,
        String direccion,
        List<MascotaResumenDTO> mascotas
) {
}   