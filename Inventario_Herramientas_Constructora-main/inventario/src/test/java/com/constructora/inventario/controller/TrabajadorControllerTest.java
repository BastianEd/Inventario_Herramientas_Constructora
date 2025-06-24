package com.constructora.inventario.controller;

import com.constructora.inventario.model.Trabajador;
import com.constructora.inventario.service.TrabajadorService;
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

@WebMvcTest(TrabajadorController.class)
public class TrabajadorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrabajadorService trabajadorService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCrearTrabajador() throws Exception {
        Trabajador nuevoTrabajador = new Trabajador(null, "Pedro Gomez", "Electricista", null);
        Trabajador trabajadorCreado = new Trabajador(1L, "Pedro Gomez", "Electricista", null);

        when(trabajadorService.guardar(any(Trabajador.class))).thenReturn(trabajadorCreado);

        mockMvc.perform(post("/api/v1/trabajadores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevoTrabajador)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Pedro Gomez"));
    }

    @Test
    public void testListarTrabajadores() throws Exception {
        List<Trabajador> trabajadores = Arrays.asList(
                new Trabajador(1L, "Pedro Gomez", "Electricista", null),
                new Trabajador(2L, "Ana Lopez", "Carpintero", null)
        );

        when(trabajadorService.listar()).thenReturn(trabajadores);

        mockMvc.perform(get("/api/v1/trabajadores")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Pedro Gomez"))
                .andExpect(jsonPath("$[1].nombre").value("Ana Lopez"));
    }

    @Test
    public void testActualizarTrabajador() throws Exception {
        Trabajador trabajadorActualizado = new Trabajador(1L, "Pedro Gomez Actualizado", "Jefe de Obra", null);

        when(trabajadorService.actualizar(anyLong(), any(Trabajador.class))).thenReturn(trabajadorActualizado);

        mockMvc.perform(put("/api/v1/trabajadores/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trabajadorActualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Pedro Gomez Actualizado"))
                .andExpect(jsonPath("$.cargo").value("Jefe de Obra"));
    }

    @Test
    public void testEliminarTrabajador() throws Exception {
        doNothing().when(trabajadorService).eliminar(1L);

        mockMvc.perform(delete("/api/v1/trabajadores/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
