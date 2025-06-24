package com.constructora.inventario.service;

import com.constructora.inventario.model.Trabajador;
import com.constructora.inventario.repository.TrabajadorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class TrabajadorServiceTest {

    @InjectMocks
    private TrabajadorService trabajadorService;

    @Mock
    private TrabajadorRepository trabajadorRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGuardarTrabajador() {
        Trabajador trabajador = new Trabajador(null, "Pedro Gomez", "Electricista", null);
        Trabajador savedTrabajador = new Trabajador(1L, "Pedro Gomez", "Electricista", null);

        when(trabajadorRepository.save(any(Trabajador.class))).thenReturn(savedTrabajador);

        Trabajador result = trabajadorService.guardar(trabajador);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Pedro Gomez", result.getNombre());
        verify(trabajadorRepository, times(1)).save(trabajador);
    }

    @Test
    public void testListarTrabajadores() {
        List<Trabajador> trabajadores = Arrays.asList(
                new Trabajador(1L, "Pedro Gomez", "Electricista", null),
                new Trabajador(2L, "Ana Lopez", "Carpintero", null)
        );

        when(trabajadorRepository.findAll()).thenReturn(trabajadores);

        List<Trabajador> result = trabajadorService.listar();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Pedro Gomez", result.get(0).getNombre());
        verify(trabajadorRepository, times(1)).findAll();
    }

    @Test
    public void testActualizarTrabajador() {
        Trabajador existingTrabajador = new Trabajador(1L, "Pedro Gomez", "Electricista", null);
        Trabajador updatedTrabajador = new Trabajador(1L, "Pedro Gomez Actualizado", "Jefe de Obra", null);

        when(trabajadorRepository.existsById(1L)).thenReturn(true);
        when(trabajadorRepository.save(any(Trabajador.class))).thenReturn(updatedTrabajador);

        Trabajador result = trabajadorService.actualizar(1L, updatedTrabajador);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Pedro Gomez Actualizado", result.getNombre());
        assertEquals("Jefe de Obra", result.getCargo());
        verify(trabajadorRepository, times(1)).existsById(1L);
        verify(trabajadorRepository, times(1)).save(updatedTrabajador);
    }

    @Test
    public void testActualizarTrabajadorNotFound() {
        Trabajador updatedTrabajador = new Trabajador(99L, "Inexistente", "Cargo", null);
        when(trabajadorRepository.existsById(99L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> trabajadorService.actualizar(99L, updatedTrabajador));
        verify(trabajadorRepository, times(1)).existsById(99L);
        verify(trabajadorRepository, never()).save(any(Trabajador.class));
    }

    @Test
    public void testEliminarTrabajador() {
        doNothing().when(trabajadorRepository).deleteById(1L);

        trabajadorService.eliminar(1L);

        verify(trabajadorRepository, times(1)).deleteById(1L);
    }
}