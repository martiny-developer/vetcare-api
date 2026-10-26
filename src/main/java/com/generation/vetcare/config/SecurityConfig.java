package com.generation.vetcare.config;

import com.generation.vetcare.security.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final List<String> origenesPermitidos;

    // Por defecto solo permite el Angular local. En Render se puede ampliar con la variable
    // APP_CORS_ALLOWED_ORIGINS (varias direcciones separadas por comas).
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                          @Value("${app.cors.allowed-origins:http://localhost:4200}") List<String> origenesPermitidos) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.origenesPermitidos = origenesPermitidos;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(origenesPermitidos);
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Rutas publicas
                        .requestMatchers("/error").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()

                        // Usuarios: solo ADMIN (lectura y escritura)
                        .requestMatchers("/api/usuarios/**").hasRole("ADMIN")

                        // Citas: cambios de estado
                        .requestMatchers(HttpMethod.PATCH, "/api/citas/*/atender").hasAnyRole("VETERINARIO", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/citas/*/cancelar").hasAnyRole("RECEPCIONISTA", "ADMIN")

                        // Veterinarios y especialidades: escritura solo ADMIN
                        .requestMatchers(HttpMethod.POST, "/api/veterinarios/**", "/api/especialidades/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/veterinarios/**", "/api/especialidades/**").hasRole("ADMIN")

                        // Duenos, mascotas y citas: escritura para recepcion y admin
                        .requestMatchers(HttpMethod.POST, "/api/duenos/**", "/api/mascotas/**", "/api/citas/**").hasAnyRole("RECEPCIONISTA", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/duenos/**", "/api/mascotas/**").hasAnyRole("RECEPCIONISTA", "ADMIN")

                        // Borrar: solo ADMIN
                        .requestMatchers(HttpMethod.DELETE, "/**").hasRole("ADMIN")

                        // Todo lo demas (lecturas): cualquier usuario autenticado
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.setCharacterEncoding("UTF-8");
                            response.getWriter().write("{\"error\": \"Token inválido, ausente o expirado\"}");
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.setContentType("application/json");
                            response.setCharacterEncoding("UTF-8");
                            response.getWriter().write("{\"error\": \"No tienes permiso para realizar esta acción\"}");
                        })
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}