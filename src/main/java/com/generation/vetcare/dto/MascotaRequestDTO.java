package com.generation.vetcare.dto;

public record MascotaRequestDTO(
        String nombre,
        String especie,
        String raza,
        Integer edad,
        Long duenoId
) {
}