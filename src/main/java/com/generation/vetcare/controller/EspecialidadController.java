package com.generation.vetcare.controller;

import com.generation.vetcare.dto.EspecialidadRequestDTO;
import com.generation.vetcare.dto.EspecialidadResponseDTO;
import com.generation.vetcare.service.EspecialidadService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/especialidades")
public class EspecialidadController {

    private final EspecialidadService especialidadService;

    public EspecialidadController(EspecialidadService especialidadService) {
        this.especialidadService = especialidadService;
    }

    @GetMapping
    public List<EspecialidadResponseDTO> listar() {
        return especialidadService.listarEspecialidades();
    }

    @GetMapping("/{id}")
    public EspecialidadResponseDTO buscarPorId(@PathVariable Long id) {
        return especialidadService.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<EspecialidadResponseDTO> crear(@Valid @RequestBody EspecialidadRequestDTO dto) {
        EspecialidadResponseDTO creada = especialidadService.crearEspecialidad(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @PutMapping("/{id}")
    public EspecialidadResponseDTO actualizar(@PathVariable Long id,
                                              @Valid @RequestBody EspecialidadRequestDTO dto) {
        return especialidadService.actualizarEspecialidad(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        especialidadService.eliminarEspecialidad(id);
        return ResponseEntity.noContent().build();
    }
}