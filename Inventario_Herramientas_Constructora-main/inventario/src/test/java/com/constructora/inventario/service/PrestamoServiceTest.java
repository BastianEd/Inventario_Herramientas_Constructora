package com.constructora.inventario.service;

import com.constructora.inventario.model.*;
import com.constructora.inventario.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PrestamoServiceTest {

    @Mock
    private PrestamoRepository prestamoRepo;

    @Mock
    private HerramientaRepository herramientaRepo;

    @Mock
    private TrabajadorRepository trabajadorRepo;

    @InjectMocks
    private PrestamoService prestamoService;

    private Herramienta herramienta;
    private Trabajador trabajador;
    private Prestamo prestamo;

    @BeforeEach
    void setUp() {
        herramienta = new Herramienta(1L, "Martillo", 10, 0, 0, null);
        trabajador = new Trabajador(1L, "Juan Perez", "AlbaÃ±il", null);
        prestamo = new Prestamo();
        prestamo.setId(1L);
        prestamo.setHerramienta(herramienta);
        prestamo.setTrabajador(trabajador);
        prestamo.setFechaPrestamo(LocalDate.now().toString());
        prestamo.setFechaDevolucion(null);
    }

    @Test
    void crearPrestamoExitoso() {
        when(herramientaRepo.findById(1L)).thenReturn(Optional.of(herramienta));
        when(trabajadorRepo.findById(1L)).thenReturn(Optional.of(trabajador));
        when(prestamoRepo.save(any(Prestamo.class))).thenReturn(prestamo);

        Prestamo resultado = prestamoService.crearPrestamo(1L, 1L);

        assertNotNull(resultado);
        verify(herramientaRepo).save(herramienta);
        assertEquals(1, herramienta.getCantidadPrestadas());
        assertEquals(9, herramienta.getCantidadDisponible());
    }

    @Test
    void devolverPrestamoExitoso() {
        herramienta.setCantidadPrestadas(1);
        herramienta.setCantidadDisponible(9);

        when(prestamoRepo.findById(1L)).thenReturn(Optional.of(prestamo));

        prestamoService.devolverPrestamo(1L);

        assertEquals(0, herramienta.getCantidadPrestadas());
        assertEquals(10, herramienta.getCantidadDisponible());
        assertNotNull(prestamo.getFechaDevolucion());
        verify(herramientaRepo).save(herramienta);
        verify(prestamoRepo).save(prestamo);
    }

    @Test
    void listarPrestamos() {
        List<Prestamo> lista = Arrays.asList(prestamo);
        when(prestamoRepo.findAll()).thenReturn(lista);

        List<Prestamo> resultado = prestamoService.listarPrestamos();

        assertEquals(1, resultado.size());
        verify(prestamoRepo).findAll();
    }

    @Test
    void actualizarPrestamoExitoso() {
        Prestamo datosActualizados = new Prestamo();
        datosActualizados.setHerramienta(herramienta);
        datosActualizados.setTrabajador(trabajador);

        when(prestamoRepo.findById(1L)).thenReturn(Optional.of(prestamo));
        when(prestamoRepo.save(any(Prestamo.class))).thenReturn(datosActualizados);

        Prestamo resultado = prestamoService.actualizarPrestamo(1L, datosActualizados);

        assertNotNull(resultado);
        verify(prestamoRepo).save(prestamo);
    }

    @Test
    void eliminarPrestamoExitoso() {
        herramienta.setCantidadPrestadas(1);

        when(prestamoRepo.findById(1L)).thenReturn(Optional.of(prestamo));
        doNothing().when(prestamoRepo).delete(prestamo);

        prestamoService.eliminar(1L);

        assertEquals(0, herramienta.getCantidadPrestadas());
        verify(herramientaRepo).save(herramienta);
        verify(prestamoRepo).delete(prestamo);
    }

    @Test
    void crearPrestamoSinDisponibilidad() {
        herramienta.setCantidadDisponible(0);
        when(herramientaRepo.findById(1L)).thenReturn(Optional.of(herramienta));
        when(trabajadorRepo.findById(1L)).thenReturn(Optional.of(trabajador));

        assertThrows(RuntimeException.class, () -> prestamoService.crearPrestamo(1L, 1L));
    }

    @Test
    void devolverPrestamoNoEncontrado() {
        when(prestamoRepo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> prestamoService.devolverPrestamo(99L));
    }
}