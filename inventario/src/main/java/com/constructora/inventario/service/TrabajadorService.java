package com.constructora.inventario.service;

import com.constructora.inventario.model.trabajador;
import com.constructora.inventario.repository.TrabajadorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrabajadorService {
    private final TrabajadorRepository repo;

    public TrabajadorService(TrabajadorRepository repo) {
        this.repo = repo;
    }

    public trabajador guardar(trabajador trabajador) {
        return repo.save(trabajador);
    }

    public List<trabajador> listar() {
        return repo.findAll();
    }

    public trabajador actualizar(Long id, trabajador trabajador) {
        if (!repo.existsById(id)) {
            throw new EntityNotFoundException("Trabajador no encontrado");
        }
        trabajador.setId(id);
        return repo.save(trabajador);
    }

    public void eliminar(Long id) {
        repo.deleteById(id);
    }
}
