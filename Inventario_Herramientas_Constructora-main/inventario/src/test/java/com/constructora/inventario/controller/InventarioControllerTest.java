package com.constructora.inventario.controller;

import com.constructora.inventario.model.Inventario;
import com.constructora.inventario.service.InventarioService;
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

@WebMvcTest(InventarioController.class)
public class InventarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InventarioService inventarioService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCrearInventario() throws Exception {
        Inventario nuevoInventario = new Inventario(null, 100, 10, 5);
        Inventario inventarioCreado = new Inventario(1L, 100, 10, 5);

        when(inventarioService.guardar(any(Inventario.class))).thenReturn(inventarioCreado);

        mockMvc.perform(post("/api/v1/inventarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevoInventario)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.stock").value(100));
    }

    @Test
    public void testListarInventarios() throws Exception {
        List<Inventario> inventarios = Arrays.asList(
                new Inventario(1L, 100, 10, 5),
                new Inventario(2L, 200, 20, 10)
        );

        when(inventarioService.listar()).thenReturn(inventarios);

        mockMvc.perform(get("/api/v1/inventarios")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].stock").value(100))
                .andExpect(jsonPath("$[1].stock").value(200));
    }

    @Test
    public void testActualizarInventario() throws Exception {
        Inventario inventarioActualizado = new Inventario(1L, 150, 15, 7);

        when(inventarioService.actualizar(anyLong(), any(Inventario.class))).thenReturn(inventarioActualizado);

        mockMvc.perform(put("/api/v1/inventarios/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inventarioActualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stock").value(150))
                .andExpect(jsonPath("$.prestadas").value(15));
    }

    @Test
    public void testEliminarInventario() throws Exception {
        doNothing().when(inventarioService).eliminar(1L);

        mockMvc.perform(delete("/api/v1/inventarios/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
