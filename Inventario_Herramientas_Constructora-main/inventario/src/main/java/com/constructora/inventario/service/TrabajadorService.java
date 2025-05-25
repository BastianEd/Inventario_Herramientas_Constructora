package com.constructora.inventario.service;

import com.constructora.inventario.model.Trabajador;
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

    public Trabajador guardar(Trabajador trabajador) {
        return repo.save(trabajador);
    }

    public List<Trabajador> listar() {
        return repo.findAll();
    }

    public Trabajador actualizar(Long id, Trabajador trabajador) {
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
