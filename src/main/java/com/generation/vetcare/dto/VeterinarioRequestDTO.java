package com.generation.vetcare.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record VeterinarioRequestDTO(
        @NotBlank(message = "El nombre es obligatorio")
        String nombre,

        @NotBlank(message = "El teléfono es obligatorio")
        String telefono,

        @Email(message = "El email no tiene un formato válido")
        String email,

        @NotEmpty(message = "El veterinario debe tener al menos una especialidad")
        List<Long> especialidadIds
) {
}