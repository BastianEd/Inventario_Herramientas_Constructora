package com.constructora.inventario.service;

import com.constructora.inventario.model.Inventario;
import com.constructora.inventario.repository.InventarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventarioService {
    private final InventarioRepository repo;

    public InventarioService(InventarioRepository repo) {
        this.repo = repo;
    }

    public Inventario guardar(Inventario inventario) {
        // Verifica los datos antes de guardarlos
        System.out.println("Guardando inventario: " + inventario);

        return repo.save(inventario);
    }

    public List<Inventario> listar() {
        return repo.findAll();
    }

    public Inventario actualizar(Long id, Inventario inventario) {
        // Verifica si el inventario existe
        if (!repo.existsById(id)) {
            throw new EntityNotFoundException("Inventario no encontrado");
        }
        inventario.setId(id);

        // Verifica los datos antes de guardarlos
        System.out.println("Actualizando inventario con ID " + id + ": " + inventario);

        return repo.save(inventario);
    }

    public void eliminar(Long id) {
        repo.deleteById(id);
    }
}
