package com.constructora.inventario.repository;

import com.constructora.inventario.model.Trabajador;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface TrabajadorRepository extends JpaRepository<Trabajador, Long> {

    @Override
    @EntityGraph(attributePaths = {"prestamos", "prestamos.herramienta", "prestamos.herramienta.categoria"})
    List<Trabajador> findAll();

    @Override
    @EntityGraph(attributePaths = {"prestamos", "prestamos.herramienta", "prestamos.herramienta.categoria"})
    Optional<Trabajador> findById(Long id);
}
