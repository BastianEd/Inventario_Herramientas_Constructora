package com.constructora.inventario.repository;

import com.constructora.inventario.model.Herramienta;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface HerramientaRepository extends JpaRepository<Herramienta, Long> {

    @Override
    @EntityGraph(attributePaths = {"categoria"})
    List<Herramienta> findAll();

    @Override
    @EntityGraph(attributePaths = {"categoria"})
    Optional<Herramienta> findById(Long id);
}
