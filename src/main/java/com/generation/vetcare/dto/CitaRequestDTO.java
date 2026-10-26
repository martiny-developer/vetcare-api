package com.generation.vetcare.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CitaRequestDTO(
        @NotNull(message = "La fecha y hora son obligatorias")
        @FutureOrPresent(message = "La cita no puede agendarse en el pasado")
        LocalDateTime fechaHora,

        @NotBlank(message = "El motivo es obligatorio")
        String motivo,

        @NotNull(message = "Debes indicar la mascota")
        Long mascotaId,

        @NotNull(message = "Debes indicar el veterinario")
        Long veterinarioId
) {
}