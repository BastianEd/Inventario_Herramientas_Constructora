package com.constructora.inventario.controller;

import com.constructora.inventario.model.Trabajador;
import com.constructora.inventario.service.TrabajadorService;
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
@RequestMapping("/api/v1/trabajadores")
@Tag(name = "Trabajador", description = "API para la gestión de trabajadores")
public class TrabajadorController {
    private final TrabajadorService service;

    public TrabajadorController(TrabajadorService service) {
        this.service = service;
    }

    @Operation(summary = "Crear un nuevo trabajador",
            description = "Crea un trabajador y lo almacena en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Trabajador creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    @PostMapping
    public ResponseEntity<Trabajador> crear(@RequestBody Trabajador nuevoTrabajador) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(nuevoTrabajador));
    }

    @Operation(summary = "Listar todos los trabajadores",
            description = "Obtiene una lista con todos los trabajadores registrados en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de trabajadores obtenida exitosamente")
    })
    @GetMapping
    public List<Trabajador> listar() {
        return service.listar();
    }

    @Operation(summary = "Actualizar los datos de un trabajador",
            description = "Permite actualizar la información de un trabajador específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trabajador actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Trabajador no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Trabajador> actualizar(@Parameter(description = "ID del trabajador a actualizar")
                                                 @PathVariable Long id,
                                                 @RequestBody Trabajador trabajador) {
        return ResponseEntity.ok(service.actualizar(id, trabajador));
    }

    @Operation(summary = "Eliminar un trabajador",
            description = "Permite eliminar un trabajador del sistema usando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Trabajador eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Trabajador no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@Parameter(description = "ID del trabajador a eliminar")
                                         @PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
