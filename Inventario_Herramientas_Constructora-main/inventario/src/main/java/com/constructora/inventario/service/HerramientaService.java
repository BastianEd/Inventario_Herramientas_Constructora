package com.constructora.inventario.service;

import com.constructora.inventario.model.Herramienta;
import com.constructora.inventario.model.Inventario;
import com.constructora.inventario.repository.HerramientaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HerramientaService {
    private final HerramientaRepository repo;
    private final InventarioService inventarioService; // Inyección del servicio de inventario

    public HerramientaService(HerramientaRepository repo, InventarioService inventarioService) {
        this.repo = repo;
        this.inventarioService = inventarioService;
    }

    public Herramienta guardar(Herramienta herramienta) {
        // Guardar la herramienta
        Herramienta nuevaHerramienta = repo.save(herramienta);

        // Actualizar el inventario
        Inventario inventario = inventarioService.listar().get(0); // Asumiendo que solo hay un inventario
        inventario.setStock(inventario.getStock() + herramienta.getCantidadDisponible());
        inventarioService.guardar(inventario); // Guardar cambios en el inventario

        return nuevaHerramienta;
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

    public void prestarHerramienta(Long id) {
        Herramienta herramienta = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Herramienta no encontrada"));

        if (herramienta.getCantidadDisponible() > 0) {
            // Incrementar la cantidad prestada
            herramienta.setCantidadPrestadas(herramienta.getCantidadPrestadas() + 1);
            // Decrementar la cantidad disponible
            herramienta.setCantidadDisponible(herramienta.getCantidadDisponible() - 1);

            // Guardar cambios en la herramienta
            repo.save(herramienta);

            // Actualizar el inventario
            Inventario inventario = inventarioService.listar().get(0); // Asumiendo que solo hay un inventario
            inventario.setStock(inventario.getStock() - 1); // Decrementar el stock en 1
            inventarioService.guardar(inventario); // Guardar cambios en el inventario
        } else {
            throw new RuntimeException("No hay herramientas disponibles para prestar");
        }
    }



    public void devolverHerramienta(Long id) {
        Herramienta herramienta = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Herramienta no encontrada"));

        if (herramienta.getCantidadPrestadas() > 0) {
            herramienta.setCantidadPrestadas(herramienta.getCantidadPrestadas() - 1);
            herramienta.setCantidadDisponible(herramienta.getCantidadDisponible() + 1);
            repo.save(herramienta); // Guardar cambios en la herramienta

            // Actualizar el inventario
            Inventario inventario = inventarioService.listar().get(0); // Asumiendo que solo hay un inventario
            inventario.setStock(inventario.getStock() + 1); // Incrementar el stock
            inventarioService.guardar(inventario); // Guardar cambios en el inventario
        } else {
            throw new RuntimeException("No hay herramientas prestadas para devolver");
        }
    }
}
