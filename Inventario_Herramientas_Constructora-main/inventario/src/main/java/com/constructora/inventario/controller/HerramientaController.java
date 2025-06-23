package com.constructora.inventario.controller;

import com.constructora.inventario.model.Herramienta;
import com.constructora.inventario.service.HerramientaService;

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
@RequestMapping("/api/v1/herramientas")
@Tag(name = "Herramienta", description = "API para la gestión de herramientas")
public class HerramientaController {

    private final HerramientaService service;

    public HerramientaController(HerramientaService service) {
        this.service = service;
    }

    @Operation(summary = "Crear una nueva herramienta",
            description = "Permite crear una nueva herramienta en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Herramienta creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    @PostMapping
    public ResponseEntity<Herramienta> crear(@RequestBody Herramienta nuevaHerramienta) {
        Herramienta herramientaCreada = service.guardar(nuevaHerramienta);
        return ResponseEntity.status(HttpStatus.CREATED).body(herramientaCreada);
    }

    @Operation(summary = "Listar todas las herramientas",
            description = "Devuelve una lista con todas las herramientas disponibles en el sistema.")
    @ApiResponse(responseCode = "200", description = "Lista de herramientas obtenida exitosamente")
    @GetMapping
    public List<Herramienta> listar() {
        return service.listar();
    }

    @Operation(summary = "Actualizar una herramienta existente",
            description = "Permite actualizar los datos de una herramienta existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Herramienta actualizada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Herramienta no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Herramienta> actualizar(
            @Parameter(description = "ID de la herramienta a actualizar") @PathVariable Long id,
            @RequestBody Herramienta herramienta) {
        return ResponseEntity.ok(service.actualizar(id, herramienta));
    }

    @Operation(summary = "Eliminar una herramienta",
            description = "Elimina una herramienta del sistema usando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Herramienta eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Herramienta no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID de la herramienta a eliminar") @PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Prestar una herramienta",
            description = "Permite prestar una herramienta. La cantidad disponible se reduce y la cantidad prestada aumenta.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Herramienta prestada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Herramienta no encontrada")
    })
    @PostMapping("/{id}/prestar")
    public ResponseEntity<Void> prestar(
            @Parameter(description = "ID de la herramienta a prestar") @PathVariable Long id) {
        service.prestarHerramienta(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Devolver una herramienta",
            description = "Permite devolver una herramienta. La cantidad disponible se incrementa y la cantidad prestada disminuye.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Herramienta devuelta exitosamente"),
            @ApiResponse(responseCode = "404", description = "Herramienta no encontrada")
    })
    @PostMapping("/{id}/devolver")
    public ResponseEntity<Void> devolver(
            @Parameter(description = "ID de la herramienta a devolver") @PathVariable Long id) {
        service.devolverHerramienta(id);
        return ResponseEntity.ok().build();
    }
}
