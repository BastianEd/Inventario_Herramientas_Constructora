package com.constructora.inventario.repository;

import com.constructora.inventario.model.CategoriaHerramienta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaHerramientaRepository extends JpaRepository<CategoriaHerramienta, Long> {
    // Aquí puedes agregar métodos personalizados si es necesario
}
