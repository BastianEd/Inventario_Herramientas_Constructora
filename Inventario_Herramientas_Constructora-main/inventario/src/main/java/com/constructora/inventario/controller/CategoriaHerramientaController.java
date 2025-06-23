package com.constructora.inventario.controller;

import com.constructora.inventario.model.CategoriaHerramienta;
import com.constructora.inventario.service.CategoriaHerramientaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categorias-herramientas")
public class CategoriaHerramientaController {

    private final CategoriaHerramientaService service;

    public CategoriaHerramientaController(CategoriaHerramientaService service) {
        this.service = service;
    }

    @Operation(summary = "Crear una nueva categoría de herramienta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Categoría creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    @PostMapping
    public ResponseEntity<CategoriaHerramienta> crear(@RequestBody CategoriaHerramienta nuevaCategoria) {
        CategoriaHerramienta categoriaCreada = service.guardar(nuevaCategoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaCreada);
    }

    @Operation(summary = "Listar todas las categorías de herramientas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de categorías obtenida exitosamente")
    })
    @GetMapping
    public List<CategoriaHerramienta> listar() {
        return service.listar();
    }

    @Operation(summary = "Obtener una categoría de herramienta por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría obtenida exitosamente"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaHerramienta> obtenerPorId(@Parameter(description = "ID de la categoría a obtener", required = true) @PathVariable Long id) {
        CategoriaHerramienta categoria = service.obtenerPorId(id);
        return ResponseEntity.ok(categoria);
    }

    @Operation(summary = "Actualizar una categoría de herramienta existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría actualizada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaHerramienta> actualizar(
            @Parameter(description = "ID de la categoría a actualizar", required = true) @PathVariable Long id,
            @RequestBody CategoriaHerramienta categoriaActualizada) {

        CategoriaHerramienta categoriaExistente = service.obtenerPorId(id); // Obtener la categoría existente
        categoriaExistente.setTipo(categoriaActualizada.getTipo());
        categoriaExistente.setMarca(categoriaActualizada.getMarca());
        categoriaExistente.setAnioAntiguedad(categoriaActualizada.getAnioAntiguedad());

        CategoriaHerramienta categoriaGuardada = service.guardar(categoriaExistente); // Guardar los cambios
        return ResponseEntity.ok(categoriaGuardada);
    }

    @Operation(summary = "Eliminar una categoría de herramienta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Categoría eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID de la categoría a eliminar", required = true) @PathVariable Long id) {

        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
