package com.constructora.inventario.controller;

import com.constructora.inventario.model.Herramienta;
import com.constructora.inventario.service.HerramientaService;
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

@WebMvcTest(HerramientaController.class)
public class HerramientaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HerramientaService herramientaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCrearHerramienta() throws Exception {
        Herramienta nuevaHerramienta = new Herramienta(null, "Martillo", 10, 0, 0, null);
        Herramienta herramientaCreada = new Herramienta(1L, "Martillo", 10, 0, 0, null);

        when(herramientaService.guardar(any(Herramienta.class))).thenReturn(herramientaCreada);

        mockMvc.perform(post("/api/v1/herramientas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevaHerramienta)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Martillo"));
    }

    @Test
    public void testListarHerramientas() throws Exception {
        List<Herramienta> herramientas = Arrays.asList(
                new Herramienta(1L, "Martillo", 10, 0, 0, null),
                new Herramienta(2L, "Taladro", 5, 0, 0, null)
        );

        when(herramientaService.listar()).thenReturn(herramientas);

        mockMvc.perform(get("/api/v1/herramientas")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Martillo"))
                .andExpect(jsonPath("$[1].nombre").value("Taladro"));
    }

    @Test
    public void testActualizarHerramienta() throws Exception {
        Herramienta herramientaActualizada = new Herramienta(1L, "Martillo Mejorado", 8, 2, 0, null);

        when(herramientaService.actualizar(anyLong(), any(Herramienta.class))).thenReturn(herramientaActualizada);

        mockMvc.perform(put("/api/v1/herramientas/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(herramientaActualizada)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Martillo Mejorado"))
                .andExpect(jsonPath("$.cantidadDisponible").value(8));
    }

    @Test
    public void testEliminarHerramienta() throws Exception {
        doNothing().when(herramientaService).eliminar(1L);

        mockMvc.perform(delete("/api/v1/herramientas/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testPrestarHerramienta() throws Exception {
        doNothing().when(herramientaService).prestarHerramienta(1L);

        mockMvc.perform(post("/api/v1/herramientas/{id}/prestar", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testDevolverHerramienta() throws Exception {
        doNothing().when(herramientaService).devolverHerramienta(1L);

        mockMvc.perform(post("/api/v1/herramientas/{id}/devolver", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
