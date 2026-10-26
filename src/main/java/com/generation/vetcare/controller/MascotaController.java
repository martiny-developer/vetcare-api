package com.generation.vetcare.controller;

import com.generation.vetcare.dto.MascotaRequestDTO;
import com.generation.vetcare.dto.MascotaResponseDTO;
import com.generation.vetcare.service.MascotaService;
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
@RequestMapping("/api/mascotas")
public class MascotaController {

    private final MascotaService mascotaService;

    public MascotaController(MascotaService mascotaService) {
        this.mascotaService = mascotaService;
    }

    @GetMapping
    public List<MascotaResponseDTO> listar() {
        return mascotaService.listarMascotas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MascotaResponseDTO> buscarPorId(@PathVariable Long id) {
        return mascotaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MascotaResponseDTO> crear(@RequestBody MascotaRequestDTO dto) {
        return mascotaService.crearMascota(dto)
                .map(creada -> ResponseEntity.status(HttpStatus.CREATED).body(creada))
                .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<MascotaResponseDTO> actualizar(@PathVariable Long id, @RequestBody MascotaRequestDTO dto) {
        return mascotaService.actualizarMascota(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        return mascotaService.eliminarMascota(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}