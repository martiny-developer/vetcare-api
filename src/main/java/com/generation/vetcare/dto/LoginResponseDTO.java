package com.generation.vetcare.dto;

import com.generation.vetcare.model.RolUsuario;

public record LoginResponseDTO(
        String token,
        String username,
        RolUsuario rol
) {
}