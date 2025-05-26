package com.constructora.inventario.service;

import com.constructora.inventario.model.Prestamo;
import com.constructora.inventario.model.Herramienta;
import com.constructora.inventario.model.Trabajador;
import com.constructora.inventario.repository.PrestamoRepository;
import com.constructora.inventario.repository.HerramientaRepository;
import com.constructora.inventario.repository.TrabajadorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PrestamoService {
    private final PrestamoRepository prestamoRepo;
    private final HerramientaRepository herramientaRepo;
    private final TrabajadorRepository trabajadorRepo;

    public PrestamoService(PrestamoRepository prestamoRepo, HerramientaRepository herramientaRepo, TrabajadorRepository trabajadorRepo) {
        this.prestamoRepo = prestamoRepo;
        this.herramientaRepo = herramientaRepo;
        this.trabajadorRepo = trabajadorRepo;
    }

    public Prestamo crearPrestamo(Long herramientaId, Long trabajadorId) {
        Herramienta herramienta = herramientaRepo.findById(herramientaId)
                .orElseThrow(() -> new EntityNotFoundException("Herramienta no encontrada"));
        Trabajador trabajador = trabajadorRepo.findById(trabajadorId)
                .orElseThrow(() -> new EntityNotFoundException("Trabajador no encontrado"));

        if (herramienta.getCantidadDisponible() <= 0) {
            throw new RuntimeException("No hay herramientas disponibles para prestar");
        }

        // Crear el préstamo
        Prestamo prestamo = new Prestamo();
        prestamo.setHerramienta(herramienta);
        prestamo.setTrabajador(trabajador);
        prestamo.setFechaPrestamo(LocalDate.now().toString()); // Establecer la fecha de préstamo
        prestamoRepo.save(prestamo);

        // Actualizar la herramienta
        herramienta.setCantidadPrestadas(herramienta.getCantidadPrestadas() + 1);
        herramienta.setCantidadDisponible(herramienta.getCantidadDisponible() - 1);
        herramientaRepo.save(herramienta); // Guardar cambios en la herramienta

        return prestamo;
    }

    public void devolverPrestamo(Long prestamoId) {
        Prestamo prestamo = prestamoRepo.findById(prestamoId)
                .orElseThrow(() -> new EntityNotFoundException("Préstamo no encontrado"));

        // Actualizar la herramienta
        Herramienta herramienta = prestamo.getHerramienta();
        herramienta.setCantidadPrestadas(herramienta.getCantidadPrestadas() - 1);
        herramienta.setCantidadDisponible(herramienta.getCantidadDisponible() + 1);
        herramientaRepo.save(herramienta); // Guardar cambios en la herramienta

        // Actualizar la fecha de devolución
        prestamo.setFechaDevolucion(LocalDate.now().toString()); // Establecer la fecha de devolución
        prestamoRepo.save(prestamo); // Guardar cambios en el préstamo
    }

    public List<Prestamo> listarPrestamos() {
        return prestamoRepo.findAll();
    }

    public Prestamo actualizarPrestamo(Long id, Prestamo prestamo) {
        Prestamo prestamoExistente = prestamoRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Préstamo no encontrado"));

        // Actualiza los campos necesarios
        prestamoExistente.setHerramienta(prestamo.getHerramienta());
        prestamoExistente.setTrabajador(prestamo.getTrabajador());
        prestamoExistente.setFechaPrestamo(prestamo.getFechaPrestamo());
        prestamoExistente.setFechaDevolucion(prestamo.getFechaDevolucion());

        return prestamoRepo.save(prestamoExistente);
    }

    public void eliminar(Long id) {
        // Verificar si el préstamo existe
        Prestamo prestamo = prestamoRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Préstamo no encontrado"));

        // Actualizar la herramienta asociada
        Herramienta herramienta = prestamo.getHerramienta();
        herramienta.setCantidadPrestadas(herramienta.getCantidadPrestadas() - 1);
        herramienta.setCantidadDisponible(herramienta.getCantidadDisponible() + 1);
        herramientaRepo.save(herramienta); // Guardar cambios en la herramienta

        // Eliminar el préstamo
        prestamoRepo.delete(prestamo);
    }

}
