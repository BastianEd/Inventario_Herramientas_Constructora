package com.constructora.inventario.controller;

import com.constructora.inventario.model.Inventario;
import com.constructora.inventario.service.InventarioService;

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
@RequestMapping("/api/v1/inventarios")
@Tag(name = "Inventario", description = "API para la gestión de inventarios")
public class InventarioController {

    private final InventarioService service;

    public InventarioController(InventarioService service) {
        this.service = service;
    }

    @Operation(summary = "Crear un nuevo inventario",
            description = "Permite crear un nuevo inventario en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Inventario creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    @PostMapping
    public ResponseEntity<Inventario> crear(@RequestBody Inventario nuevoInventario) {
        Inventario inventarioCreado = service.guardar(nuevoInventario);
        return ResponseEntity.status(HttpStatus.CREATED).body(inventarioCreado);
    }

    @Operation(summary = "Listar todos los inventarios",
            description = "Devuelve una lista con todos los inventarios disponibles en el sistema.")
    @ApiResponse(responseCode = "200", description = "Lista de inventarios obtenida exitosamente")
    @GetMapping
    public List<Inventario> listar() {
        return service.listar();
    }

    @Operation(summary = "Actualizar un inventario existente",
            description = "Permite actualizar los datos de un inventario existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventario actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Inventario no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Inventario> actualizar(
            @Parameter(description = "ID del inventario a actualizar") @PathVariable Long id,
            @RequestBody Inventario inventario) {
        return ResponseEntity.ok(service.actualizar(id, inventario));
    }

    @Operation(summary = "Eliminar un inventario",
            description = "Elimina un inventario del sistema usando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Inventario eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Inventario no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del inventario a eliminar") @PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
