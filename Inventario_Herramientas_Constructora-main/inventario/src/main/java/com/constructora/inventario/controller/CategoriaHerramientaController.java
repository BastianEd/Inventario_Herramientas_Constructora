package com.constructora.inventario.controller;

import com.constructora.inventario.model.CategoriaHerramienta;
import com.constructora.inventario.service.CategoriaHerramientaService;
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

    @PostMapping
    public ResponseEntity<CategoriaHerramienta> crear(@RequestBody CategoriaHerramienta nuevaCategoria) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(nuevaCategoria));
    }

    @GetMapping
    public List<CategoriaHerramienta> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaHerramienta> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaHerramienta> actualizar(@PathVariable Long id, @RequestBody CategoriaHerramienta categoriaActualizada) {
        CategoriaHerramienta categoria = service.obtenerPorId(id); // Obtener la categoría existente
        categoria.setTipo(categoriaActualizada.getTipo());
        categoria.setMarca(categoriaActualizada.getMarca());
        categoria.setAnioAntiguedad(categoriaActualizada.getAnioAntiguedad());

        CategoriaHerramienta categoriaGuardada = service.guardar(categoria); // Guardar los cambios
        return ResponseEntity.ok(categoriaGuardada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
