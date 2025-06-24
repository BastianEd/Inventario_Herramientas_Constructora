package com.constructora.inventario.controller;

import com.constructora.inventario.model.CategoriaHerramienta;
import com.constructora.inventario.service.CategoriaHerramientaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoriaHerramientaController.class)
public class CategoriaHerramientaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoriaHerramientaService categoriaHerramientaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCrearCategoria() throws Exception {
        CategoriaHerramienta nuevaCategoria = new CategoriaHerramienta(null, "Eléctrica", "Bosch", 2);
        CategoriaHerramienta categoriaCreada = new CategoriaHerramienta(1L, "Eléctrica", "Bosch", 2);

        when(categoriaHerramientaService.guardar(any(CategoriaHerramienta.class))).thenReturn(categoriaCreada);

        mockMvc.perform(post("/api/v1/categorias-herramientas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevaCategoria)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.tipo").value("Eléctrica"));
    }

    @Test
    public void testListarCategorias() throws Exception {
        List<CategoriaHerramienta> categorias = Arrays.asList(
                new CategoriaHerramienta(1L, "Manual", "Stanley", 5),
                new CategoriaHerramienta(2L, "Pesada", "Caterpillar", 10)
        );

        when(categoriaHerramientaService.listar()).thenReturn(categorias);

        mockMvc.perform(get("/api/v1/categorias-herramientas")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tipo").value("Manual"))
                .andExpect(jsonPath("$[1].tipo").value("Pesada"));
    }

    @Test
    public void testObtenerCategoriaPorId() throws Exception {
        CategoriaHerramienta categoria = new CategoriaHerramienta(1L, "Industrial", "Makita", 3);

        when(categoriaHerramientaService.obtenerPorId(1L)).thenReturn(categoria);

        mockMvc.perform(get("/api/v1/categorias-herramientas/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.tipo").value("Industrial"));
    }

    @Test
    public void testActualizarCategoria() throws Exception {
        CategoriaHerramienta categoriaExistente = new CategoriaHerramienta(1L, "Manual", "Stanley", 5);
        CategoriaHerramienta categoriaActualizada = new CategoriaHerramienta(1L, "Manual", "Stanley", 6);

        when(categoriaHerramientaService.obtenerPorId(1L)).thenReturn(categoriaExistente);
        when(categoriaHerramientaService.guardar(any(CategoriaHerramienta.class))).thenReturn(categoriaActualizada);

        mockMvc.perform(put("/api/v1/categorias-herramientas/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoriaActualizada)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.anioAntiguedad").value(6));
    }

    @Test
    public void testEliminarCategoria() throws Exception {
        doNothing().when(categoriaHerramientaService).eliminar(1L);

        mockMvc.perform(delete("/api/v1/categorias-herramientas/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
