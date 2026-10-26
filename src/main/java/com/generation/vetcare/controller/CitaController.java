package com.generation.vetcare.controller;

import com.generation.vetcare.dto.CitaRequestDTO;
import com.generation.vetcare.dto.CitaResponseDTO;
import com.generation.vetcare.service.CitaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/citas")
public class CitaController {

    private final CitaService citaService;

    public CitaController(CitaService citaService) {
        this.citaService = citaService;
    }

    @GetMapping
    public List<CitaResponseDTO> listar() {
        return citaService.listarCitas();
    }

    @GetMapping("/{id}")
    public CitaResponseDTO buscarPorId(@PathVariable Long id) {
        return citaService.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<CitaResponseDTO> crear(@Valid @RequestBody CitaRequestDTO dto) {
        return citaService.crearCita(dto)
                .map(creada -> ResponseEntity.status(HttpStatus.CREATED).body(creada))
                .orElse(ResponseEntity.badRequest().build());
    }

    @PatchMapping("/{id}/atender")
    public CitaResponseDTO atender(@PathVariable Long id) {
        return citaService.marcarComoAtendida(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        citaService.eliminarCita(id);
        return ResponseEntity.noContent().build();
    }
}