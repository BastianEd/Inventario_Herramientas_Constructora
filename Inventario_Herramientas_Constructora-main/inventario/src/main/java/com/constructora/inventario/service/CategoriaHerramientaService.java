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

    public CategoriaHerramienta guardar(CategoriaHerramienta categoria) {
        return repo.save(categoria);
    }

    public List<CategoriaHerramienta> listar() {
        return repo.findAll();
    }

    public CategoriaHerramienta actualizar(Long id, CategoriaHerramienta categoria) {
        if (!repo.existsById(id)) {
            throw new EntityNotFoundException("Categoría no encontrada");
        }
        categoria.getId(id);
        return repo.save(categoria);
    }

    public void eliminar(Long id) {
        repo.deleteById(id);
    }
}
