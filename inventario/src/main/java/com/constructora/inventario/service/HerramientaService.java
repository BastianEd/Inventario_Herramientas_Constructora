package com.constructora.inventario.service;

import com.constructora.inventario.model.Herramienta;
import com.constructora.inventario.repository.HerramientaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HerramientaService {
    private final HerramientaRepository repo;

    public HerramientaService(HerramientaRepository repo) {
        this.repo = repo;
    }

    public Herramienta guardar(Herramienta herramienta) {
        return repo.save(herramienta);
    }

    public List<Herramienta> listar() {
        return repo.findAll();
    }

    public Herramienta actualizar(Long id, Herramienta herramienta) {
        if (!repo.existsById(id)) {
            throw new EntityNotFoundException("Herramienta no encontrada");
        }
        herramienta.setId(id);
        return repo.save(herramienta);
    }

    public void eliminar(Long id) {
        repo.deleteById(id);
    }
}
