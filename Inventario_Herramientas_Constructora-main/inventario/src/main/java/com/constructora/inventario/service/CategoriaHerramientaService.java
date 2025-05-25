package com.constructora.inventario.service;

import com.constructora.inventario.model.CategoriaHerramienta;
import com.constructora.inventario.repository.CategoriaHerramientaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaHerramientaService {
    private final CategoriaHerramientaRepository repo;

    public CategoriaHerramientaService(CategoriaHerramientaRepository repo) {
        this.repo = repo;
    }

    public CategoriaHerramienta guardar(CategoriaHerramienta categoriaHerramienta) {
        return repo.save(categoriaHerramienta);
    }

    public List<CategoriaHerramienta> listar() {
        return repo.findAll();
    }

    public CategoriaHerramienta obtenerPorId(Long id) {
        return repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Categoría de herramienta no encontrada"));
    }

    public void eliminar(Long id) {
        repo.deleteById(id);
    }
}
