package com.constructora.inventario.repository;
import com.constructora.inventario.model.Trabajador;
import org.springframework.data.jpa.repository.JpaRepository;
public interface TrabajadorRepository extends JpaRepository<Trabajador, Long> {
}
