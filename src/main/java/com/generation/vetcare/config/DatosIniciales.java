package com.generation.vetcare.config;

import com.generation.vetcare.model.RolUsuario;
import com.generation.vetcare.model.Usuario;
import com.generation.vetcare.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DatosIniciales {

    private static final Logger log = LoggerFactory.getLogger(DatosIniciales.class);

    // Contraseña de demostración (pública). Para cambiarla, edita el texto después de los dos puntos.
    @Bean
    CommandLineRunner crearAdminInicial(UsuarioRepository usuarioRepository,
                                        PasswordEncoder passwordEncoder,
                                        @Value("${ADMIN_PASSWORD:Vetcare2026}") String passwordAdmin) {
        return args -> {
            if (usuarioRepository.existsByUsername("admin")) {
                return;
            }

            Usuario admin = new Usuario();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode(passwordAdmin));
            admin.setRol(RolUsuario.ADMIN);
            usuarioRepository.save(admin);
            log.info("Usuario admin creado");
        };
    }
}