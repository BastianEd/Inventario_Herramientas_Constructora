package com.constructora.inventario.controller;

import com.constructora.inventario.model.Prestamo;
import com.constructora.inventario.service.PrestamoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/prestamos")
@Tag(name = "Préstamo", description = "API para la gestión de préstamos de herramientas")
public class PrestamoController {
    private final PrestamoService service;

    public PrestamoController(PrestamoService service) {
        this.service = service;
    }

    @Operation(summary = "Crear un nuevo préstamo de herramienta",
            description = "Permite crear un nuevo préstamo de herramienta a un trabajador.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Préstamo creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "404", description = "Herramienta o Trabajador no encontrado")
    })
    @PostMapping
    public ResponseEntity<Prestamo> crearPrestamo(
            @RequestParam Long herramientaId,
            @RequestParam Long trabajadorId) {
        Prestamo prestamoCreado = service.crearPrestamo(herramientaId, trabajadorId);
        return ResponseEntity.status(HttpStatus.CREATED).body(prestamoCreado);
    }

    @Operation(summary = "Actualizar un préstamo existente",
            description = "Permite actualizar los detalles de un préstamo de herramienta.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Préstamo actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Préstamo no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Prestamo> actualizarPrestamo(
            @Parameter(description = "ID del préstamo a actualizar") @PathVariable Long id,
            @RequestBody Prestamo prestamo) {
        Prestamo prestamoActualizado = service.actualizarPrestamo(id, prestamo);
        return ResponseEntity.ok(prestamoActualizado);
    }

    @Operation(summary = "Devolver un préstamo",
            description = "Permite devolver una herramienta prestada.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Herramienta devuelta exitosamente"),
            @ApiResponse(responseCode = "404", description = "Préstamo no encontrado")
    })
    @PostMapping("/{id}/devolver")
    public ResponseEntity<Void> devolverPrestamo(@PathVariable Long id) {
        service.devolverPrestamo(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Listar todos los préstamos",
            description = "Devuelve una lista con todos los préstamos registrados en el sistema.")
    @ApiResponse(responseCode = "200", description = "Lista de préstamos obtenida exitosamente")
    @GetMapping
    public List<Prestamo> listarPrestamos() {
        return service.listarPrestamos();
    }

    @Operation(summary = "Eliminar un préstamo",
            description = "Permite eliminar un préstamo de herramienta.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Préstamo eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Préstamo no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPrestamo(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
