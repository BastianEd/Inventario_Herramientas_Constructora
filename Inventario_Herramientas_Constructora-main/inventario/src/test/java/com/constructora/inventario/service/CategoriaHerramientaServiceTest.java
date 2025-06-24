package com.constructora.inventario.service;

import com.constructora.inventario.model.CategoriaHerramienta;
import com.constructora.inventario.repository.CategoriaHerramientaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class CategoriaHerramientaServiceTest {

    @InjectMocks
    private CategoriaHerramientaService categoriaHerramientaService;

    @Mock
    private CategoriaHerramientaRepository categoriaHerramientaRepository;

    private CategoriaHerramienta categoriaEjemplo;

    @BeforeEach
    void setUp() {
        categoriaEjemplo = new CategoriaHerramienta(1L, "Eléctrica", "Bosch", 2);
    }

    @Test
    public void testGuardarCategoria() {
        when(categoriaHerramientaRepository.save(any(CategoriaHerramienta.class))).thenReturn(categoriaEjemplo);

        CategoriaHerramienta resultado = categoriaHerramientaService.guardar(categoriaEjemplo);

        assertNotNull(resultado);
        assertEquals("Eléctrica", resultado.getTipo());
        verify(categoriaHerramientaRepository, times(1)).save(categoriaEjemplo);
    }

    @Test
    public void testListarCategorias() {
        List<CategoriaHerramienta> categorias = Arrays.asList(
                categoriaEjemplo,
                new CategoriaHerramienta(2L, "Manual", "Stanley", 5)
        );

        when(categoriaHerramientaRepository.findAll()).thenReturn(categorias);

        List<CategoriaHerramienta> resultado = categoriaHerramientaService.listar();

        assertEquals(2, resultado.size());
        verify(categoriaHerramientaRepository, times(1)).findAll();
    }

    @Test
    public void testObtenerPorId() {
        when(categoriaHerramientaRepository.findById(1L)).thenReturn(Optional.of(categoriaEjemplo));

        CategoriaHerramienta resultado = categoriaHerramientaService.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    public void testObtenerPorIdNotFound() {
        when(categoriaHerramientaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            categoriaHerramientaService.obtenerPorId(99L);
        });
    }

    @Test
    public void testEliminarCategoria() {
        doNothing().when(categoriaHerramientaRepository).deleteById(1L);

        categoriaHerramientaService.eliminar(1L);

        verify(categoriaHerramientaRepository, times(1)).deleteById(1L);
    }
}