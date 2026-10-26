package com.generation.vetcare.dto;

import com.generation.vetcare.model.RolUsuario;

public record UsuarioResponseDTO(
        Long id,
        String username,
        RolUsuario rol
) {
}