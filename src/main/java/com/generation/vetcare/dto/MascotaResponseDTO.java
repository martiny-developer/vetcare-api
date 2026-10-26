package com.generation.vetcare.dto;

public record MascotaResponseDTO(
        Long id,
        String nombre,
        String especie,
        String raza,
        Integer edad,
        DuenoResumenDTO dueno
) {
}