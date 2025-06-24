package com.constructora.inventario.controller;

import com.constructora.inventario.model.Herramienta;
import com.constructora.inventario.model.Prestamo;
import com.constructora.inventario.model.Trabajador;
import com.constructora.inventario.service.PrestamoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PrestamoController.class)
public class PrestamoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PrestamoService prestamoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCrearPrestamo() throws Exception {
        Herramienta herramienta = new Herramienta(1L, "Martillo", 10, 0, 0, null);
        Trabajador trabajador = new Trabajador(1L, "Juan Perez", "Albañil", null);
        Prestamo prestamoCreado = new Prestamo();
        prestamoCreado.setId(1L);
        prestamoCreado.setHerramienta(herramienta);
        prestamoCreado.setTrabajador(trabajador);
        prestamoCreado.setFechaPrestamo(LocalDate.now().toString());

        when(prestamoService.crearPrestamo(anyLong(), anyLong())).thenReturn(prestamoCreado);

        mockMvc.perform(post("/api/v1/prestamos")
                        .param("herramientaId", "1")
                        .param("trabajadorId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.herramienta.nombre").value("Martillo"));
    }

    @Test
    public void testActualizarPrestamo() throws Exception {
        Herramienta herramienta = new Herramienta(1L, "Martillo", 9, 1, 0, null);
        Trabajador trabajador = new Trabajador(1L, "Juan Perez", "Albañil", null);
        Prestamo prestamoActualizado = new Prestamo();
        prestamoActualizado.setId(1L);
        prestamoActualizado.setHerramienta(herramienta);
        prestamoActualizado.setTrabajador(trabajador);
        prestamoActualizado.setFechaPrestamo(LocalDate.now().minusDays(5).toString());
        prestamoActualizado.setFechaDevolucion(LocalDate.now().toString());

        when(prestamoService.actualizarPrestamo(anyLong(), any(Prestamo.class))).thenReturn(prestamoActualizado);

        mockMvc.perform(put("/api/v1/prestamos/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(prestamoActualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fechaDevolucion").exists());
    }

    @Test
    public void testDevolverPrestamo() throws Exception {
        doNothing().when(prestamoService).devolverPrestamo(1L);

        mockMvc.perform(post("/api/v1/prestamos/{id}/devolver", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testListarPrestamos() throws Exception {
        Herramienta herramienta1 = new Herramienta(1L, "Martillo", 10, 0, 0, null);
        Trabajador trabajador1 = new Trabajador(1L, "Juan Perez", "Albañil", null);
        Prestamo prestamo1 = new Prestamo();
        prestamo1.setId(1L);
        prestamo1.setHerramienta(herramienta1);
        prestamo1.setTrabajador(trabajador1);
        prestamo1.setFechaPrestamo(LocalDate.now().toString());

        List<Prestamo> prestamos = Arrays.asList(prestamo1);

        when(prestamoService.listarPrestamos()).thenReturn(prestamos);

        mockMvc.perform(get("/api/v1/prestamos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    public void testEliminarPrestamo() throws Exception {
        doNothing().when(prestamoService).eliminar(1L);

        mockMvc.perform(delete("/api/v1/prestamos/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
