package com.generation.vetcare.repository;

import com.generation.vetcare.model.Dueno;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DuenoRepository extends JpaRepository<Dueno, Long> {
}