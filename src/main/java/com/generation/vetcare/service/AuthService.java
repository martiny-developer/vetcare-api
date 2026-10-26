package com.generation.vetcare.service;

import com.generation.vetcare.dto.LoginRequestDTO;
import com.generation.vetcare.dto.LoginResponseDTO;
import com.generation.vetcare.exception.CredencialesInvalidasException;
import com.generation.vetcare.model.Usuario;
import com.generation.vetcare.repository.UsuarioRepository;
import com.generation.vetcare.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginResponseDTO iniciarSesion(LoginRequestDTO datos) {
        Usuario usuario = usuarioRepository.findByUsername(datos.username())
                .orElseThrow(() -> new CredencialesInvalidasException("Usuario o contraseña incorrectos"));

        if (!passwordEncoder.matches(datos.password(), usuario.getPassword())) {
            throw new CredencialesInvalidasException("Usuario o contraseña incorrectos");
        }

        String token = jwtService.generarToken(usuario);
        return new LoginResponseDTO(token, usuario.getUsername(), usuario.getRol());
    }
}