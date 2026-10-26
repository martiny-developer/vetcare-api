package com.generation.vetcare.dto;

import jakarta.validation.constraints.NotBlank;

public record EspecialidadRequestDTO(
        @NotBlank(message = "El nombre de la especialidad es obligatorio")
        String nombre
) {
}