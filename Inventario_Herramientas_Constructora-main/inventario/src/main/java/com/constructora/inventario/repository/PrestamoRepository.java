package com.constructora.inventario.repository;

import com.constructora.inventario.model.Herramienta;
import com.constructora.inventario.model.Prestamo;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {
    long countByHerramientaAndFechaDevolucionIsNull(Herramienta herramienta);

    @Override
    @EntityGraph(attributePaths = {"herramienta", "trabajador", "herramienta.categoria"})
    List<Prestamo> findAll();

    @Override
    @EntityGraph(attributePaths = {"herramienta", "trabajador", "herramienta.categoria"})
    Optional<Prestamo> findById(Long id);
}
