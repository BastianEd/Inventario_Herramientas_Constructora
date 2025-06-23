package com.constructora.inventario.service;

import com.constructora.inventario.model.CategoriaHerramienta;
import com.constructora.inventario.repository.CategoriaHerramientaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CategoriaHerramientaServiceTest {

    @Autowired
    private CategoriaHerramientaService categoriaHerramientaService;

    @MockBean
    private CategoriaHerramientaRepository categoriaHerramientaRepository;

    @Test
    public void testGuardar() {
        CategoriaHerramienta categoria = new CategoriaHerramienta(null, "Eléctrica", "Bosch", 2);

        when(categoriaHerramientaRepository.save(categoria)).thenReturn(
                new CategoriaHerramienta(1L, "Eléctrica", "Bosch", 2)
        );

        CategoriaHerramienta guardada = categoriaHerramientaService.guardar(categoria);

        assertNotNull(guardada);
        assertEquals("Eléctrica", guardada.getTipo());
        assertEquals("Bosch", guardada.getMarca());
        assertEquals(2, guardada.getAnioAntiguedad());
    }

    @Test
    public void testListar() {
        when(categoriaHerramientaRepository.findAll()).thenReturn(
                List.of(new CategoriaHerramienta(1L, "Manual", "Stanley", 5))
        );

        List<CategoriaHerramienta> lista = categoriaHerramientaService.listar();

        assertNotNull(lista);
        assertEquals(1, lista.size());
        assertEquals("Manual", lista.get(0).getTipo());
    }

    @Test
    public void testObtenerPorId() {
        CategoriaHerramienta categoria = new CategoriaHerramienta(1L, "Industrial", "Makita", 3);
        when(categoriaHerramientaRepository.findById(1L)).thenReturn(Optional.of(categoria));

        CategoriaHerramienta resultado = categoriaHerramientaService.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals("Industrial", resultado.getTipo());
    }

    @Test
    public void testEliminar() {
        doNothing().when(categoriaHerramientaRepository).deleteById(1L);

        categoriaHerramientaService.eliminar(1L);

        verify(categoriaHerramientaRepository, times(1)).deleteById(1L);
    }
}
