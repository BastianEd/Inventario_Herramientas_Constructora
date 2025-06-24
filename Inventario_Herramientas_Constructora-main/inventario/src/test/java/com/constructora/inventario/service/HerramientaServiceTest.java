package com.constructora.inventario.service;

import com.constructora.inventario.model.Herramienta;
import com.constructora.inventario.model.Inventario;
import com.constructora.inventario.repository.HerramientaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class HerramientaServiceTest {

    private HerramientaService herramientaService;
    private HerramientaRepository herramientaRepository;
    private InventarioService inventarioService;

    private Herramienta herramientaEjemplo;
    private Inventario inventarioEjemplo;

    @BeforeEach
    void setUp() {
        herramientaRepository = mock(HerramientaRepository.class);
        inventarioService = mock(InventarioService.class);
        herramientaService = new HerramientaService(herramientaRepository, inventarioService);

        herramientaEjemplo = new Herramienta(1L, "Martillo", 10, 0, 0, null);
        inventarioEjemplo = new Inventario(1L, 100, 0, 0);

        when(inventarioService.listar()).thenReturn(Arrays.asList(inventarioEjemplo));
        when(inventarioService.guardar(any(Inventario.class))).thenReturn(inventarioEjemplo);
    }

    @Test
    public void testGuardarHerramienta() {
        when(herramientaRepository.save(any(Herramienta.class))).thenReturn(herramientaEjemplo);

        Herramienta savedHerramienta = herramientaService.guardar(herramientaEjemplo);

        assertNotNull(savedHerramienta);
        assertEquals("Martillo", savedHerramienta.getNombre());
        verify(herramientaRepository, times(1)).save(herramientaEjemplo);
        verify(inventarioService, times(1)).guardar(any(Inventario.class));
    }

    @Test
    public void testListarHerramientas() {
        List<Herramienta> herramientas = Arrays.asList(
                herramientaEjemplo,
                new Herramienta(2L, "Taladro", 5, 0, 0, null)
        );
        when(herramientaRepository.findAll()).thenReturn(herramientas);

        List<Herramienta> foundHerramientas = herramientaService.listar();

        assertNotNull(foundHerramientas);
        assertEquals(2, foundHerramientas.size());
        verify(herramientaRepository, times(1)).findAll();
    }

    @Test
    public void testActualizarHerramienta() {
        Herramienta updatedHerramienta = new Herramienta(1L, "Martillo Mejorado", 8, 2, 0, null);
        when(herramientaRepository.existsById(1L)).thenReturn(true);
        when(herramientaRepository.save(any(Herramienta.class))).thenReturn(updatedHerramienta);

        Herramienta result = herramientaService.actualizar(1L, updatedHerramienta);

        assertNotNull(result);
        assertEquals("Martillo Mejorado", result.getNombre());
        verify(herramientaRepository, times(1)).existsById(1L);
        verify(herramientaRepository, times(1)).save(updatedHerramienta);
    }

    @Test
    public void testActualizarHerramientaNotFound() {
        Herramienta updatedHerramienta = new Herramienta(99L, "Inexistente", 1, 0, 0, null);
        when(herramientaRepository.existsById(99L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> herramientaService.actualizar(99L, updatedHerramienta));
        verify(herramientaRepository, times(1)).existsById(99L);
        verify(herramientaRepository, never()).save(any(Herramienta.class));
    }

    @Test
    public void testEliminarHerramienta() {
        doNothing().when(herramientaRepository).deleteById(1L);

        herramientaService.eliminar(1L);

        verify(herramientaRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testPrestarHerramienta() {
        herramientaEjemplo.setCantidadDisponible(10);
        herramientaEjemplo.setCantidadPrestadas(0);
        inventarioEjemplo.setStock(100);

        when(herramientaRepository.findById(1L)).thenReturn(Optional.of(herramientaEjemplo));
        when(herramientaRepository.save(any(Herramienta.class))).thenReturn(herramientaEjemplo);

        herramientaService.prestarHerramienta(1L);

        assertEquals(9, herramientaEjemplo.getCantidadDisponible());
        assertEquals(1, herramientaEjemplo.getCantidadPrestadas());
        assertEquals(99, inventarioEjemplo.getStock());
        verify(herramientaRepository, times(1)).save(herramientaEjemplo);
        verify(inventarioService, times(1)).guardar(inventarioEjemplo);
    }

    @Test
    public void testPrestarHerramientaNoDisponible() {
        herramientaEjemplo.setCantidadDisponible(0);
        when(herramientaRepository.findById(1L)).thenReturn(Optional.of(herramientaEjemplo));

        assertThrows(RuntimeException.class, () -> herramientaService.prestarHerramienta(1L));
        verify(herramientaRepository, never()).save(any(Herramienta.class));
        verify(inventarioService, never()).guardar(any(Inventario.class));
    }

    @Test
    public void testDevolverHerramienta() {
        herramientaEjemplo.setCantidadDisponible(9);
        herramientaEjemplo.setCantidadPrestadas(1);
        inventarioEjemplo.setStock(99);

        when(herramientaRepository.findById(1L)).thenReturn(Optional.of(herramientaEjemplo));
        when(herramientaRepository.save(any(Herramienta.class))).thenReturn(herramientaEjemplo);

        herramientaService.devolverHerramienta(1L);

        assertEquals(10, herramientaEjemplo.getCantidadDisponible());
        assertEquals(0, herramientaEjemplo.getCantidadPrestadas());
        assertEquals(100, inventarioEjemplo.getStock());
        verify(herramientaRepository, times(1)).save(herramientaEjemplo);
        verify(inventarioService, times(1)).guardar(inventarioEjemplo);
    }

    @Test
    public void testDevolverHerramientaNoPrestada() {
        herramientaEjemplo.setCantidadPrestadas(0);
        when(herramientaRepository.findById(1L)).thenReturn(Optional.of(herramientaEjemplo));

        assertThrows(RuntimeException.class, () -> herramientaService.devolverHerramienta(1L));
        verify(herramientaRepository, never()).save(any(Herramienta.class));
        verify(inventarioService, never()).guardar(any(Inventario.class));
    }
}