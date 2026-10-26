package com.generation.vetcare.dto;

import com.generation.vetcare.model.EstadoCita;

import java.time.LocalDateTime;

public record CitaResponseDTO(
        Long id,
        LocalDateTime fechaHora,
        String motivo,
        EstadoCita estado,
        MascotaResumenDTO mascota,
        VeterinarioResumenDTO veterinario
) {
}