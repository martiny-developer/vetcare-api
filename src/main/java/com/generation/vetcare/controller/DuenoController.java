package com.generation.vetcare.controller;

import com.generation.vetcare.dto.DuenoRequestDTO;
import com.generation.vetcare.dto.DuenoResponseDTO;
import com.generation.vetcare.service.DuenoService;
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
@RequestMapping("/api/duenos")
public class DuenoController {

    private final DuenoService duenoService;

    public DuenoController(DuenoService duenoService) {
        this.duenoService = duenoService;
    }

    @GetMapping
    public List<DuenoResponseDTO> listar() {
        return duenoService.listarDuenos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DuenoResponseDTO> buscarPorId(@PathVariable Long id) {
        return duenoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<DuenoResponseDTO> crear(@RequestBody DuenoRequestDTO dto) {
        DuenoResponseDTO creado = duenoService.crearDueno(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DuenoResponseDTO> actualizar(@PathVariable Long id, @RequestBody DuenoRequestDTO dto) {
        return duenoService.actualizarDueno(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        return duenoService.eliminarDueno(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}