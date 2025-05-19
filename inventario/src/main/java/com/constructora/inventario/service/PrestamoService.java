package com.constructora.inventario.service;

import com.constructora.inventario.model.Prestamo;
import com.constructora.inventario.repository.PrestamoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrestamoService {
    private final PrestamoRepository repo;

    public PrestamoService(PrestamoRepository repo) {
        this.repo = repo;
    }

    public Prestamo guardar(Prestamo prestamo) {
        return repo.save(prestamo);
    }

    public List<Prestamo> listar() {
        return repo.findAll();
    }

    public Prestamo devolver(Long id) {
        Prestamo prestamo = repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Préstamo no encontrado"));
        prestamo.setFechaDevolucion("fecha actual"); // Implementar lógica para establecer la fecha actual
        return repo.save(prestamo);
    }

    public void eliminar(Long id) {
        repo.deleteById(id);
    }
}
