package com.generation.vetcare.controller;

import com.generation.vetcare.dto.VeterinarioRequestDTO;
import com.generation.vetcare.dto.VeterinarioResponseDTO;
import com.generation.vetcare.service.VeterinarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/veterinarios")
public class VeterinarioController {

    private final VeterinarioService veterinarioService;

    public VeterinarioController(VeterinarioService veterinarioService) {
        this.veterinarioService = veterinarioService;
    }

    @GetMapping
    public List<VeterinarioResponseDTO> listar() {
        return veterinarioService.listarVeterinarios();
    }

    @GetMapping("/{id}")
    public VeterinarioResponseDTO buscarPorId(@PathVariable Long id) {
        return veterinarioService.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<VeterinarioResponseDTO> crear(@Valid @RequestBody VeterinarioRequestDTO dto) {
        VeterinarioResponseDTO creado = veterinarioService.crearVeterinario(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        veterinarioService.eliminarVeterinario(id);
        return ResponseEntity.noContent().build();
    }
}