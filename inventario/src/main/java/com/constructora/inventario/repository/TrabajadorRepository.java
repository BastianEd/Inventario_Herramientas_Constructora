package com.constructora.inventario.repository;
import com.constructora.inventario.model.trabajador;
import org.springframework.data.jpa.repository.JpaRepository;
public interface TrabajadorRepository extends JpaRepository<trabajador, Long> {
}
