package com.constructora.inventario.service;

import com.constructora.inventario.model.Inventario;
import com.constructora.inventario.repository.InventarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class InventarioServiceTest {

    @Autowired
    private InventarioService inventarioService;

    @MockBean
    private InventarioRepository inventarioRepository;

    @Test
    public void testGuardarInventario() {
        Inventario inventario = new Inventario(null, 100, 10, 5);
        Inventario savedInventario = new Inventario(1L, 100, 10, 5);

        when(inventarioRepository.save(any(Inventario.class))).thenReturn(savedInventario);

        Inventario result = inventarioService.guardar(inventario);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(100, result.getStock());
        verify(inventarioRepository, times(1)).save(inventario);
    }

    @Test
    public void testListarInventarios() {
        List<Inventario> inventarios = Arrays.asList(
                new Inventario(1L, 100, 10, 5),
                new Inventario(2L, 200, 20, 10)
        );

        when(inventarioRepository.findAll()).thenReturn(inventarios);

        List<Inventario> result = inventarioService.listar();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(100, result.get(0).getStock());
        verify(inventarioRepository, times(1)).findAll();
    }

    @Test
    public void testActualizarInventario() {
        Inventario existingInventario = new Inventario(1L, 100, 10, 5);
        Inventario updatedInventario = new Inventario(1L, 150, 15, 7);

        when(inventarioRepository.existsById(1L)).thenReturn(true);
        when(inventarioRepository.save(any(Inventario.class))).thenReturn(updatedInventario);

        Inventario result = inventarioService.actualizar(1L, updatedInventario);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(150, result.getStock());
        assertEquals(15, result.getPrestadas());
        verify(inventarioRepository, times(1)).existsById(1L);
        verify(inventarioRepository, times(1)).save(updatedInventario);
    }

    @Test
    public void testActualizarInventarioNotFound() {
        Inventario updatedInventario = new Inventario(99L, 150, 15, 7);
        when(inventarioRepository.existsById(99L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> inventarioService.actualizar(99L, updatedInventario));
        verify(inventarioRepository, times(1)).existsById(99L);
        verify(inventarioRepository, never()).save(any(Inventario.class));
    }

    @Test
    public void testEliminarInventario() {
        doNothing().when(inventarioRepository).deleteById(1L);

        inventarioService.eliminar(1L);

        verify(inventarioRepository, times(1)).deleteById(1L);
    }
}

