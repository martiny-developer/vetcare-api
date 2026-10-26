package com.generation.vetcare.service;

import com.generation.vetcare.dto.UsuarioRequestDTO;
import com.generation.vetcare.dto.UsuarioResponseDTO;
import com.generation.vetcare.exception.UsuarioDuplicadoException;
import com.generation.vetcare.model.Usuario;
import com.generation.vetcare.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UsuarioResponseDTO crearUsuario(UsuarioRequestDTO datos) {
        if (usuarioRepository.findByUsername(datos.username()).isPresent()) {
            throw new UsuarioDuplicadoException("Ya existe una cuenta con el username " + datos.username());
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(datos.username());
        usuario.setPassword(passwordEncoder.encode(datos.password()));
        usuario.setRol(datos.rol());

        Usuario creado = usuarioRepository.save(usuario);
        return new UsuarioResponseDTO(creado.getId(), creado.getUsername(), creado.getRol());
    }
}