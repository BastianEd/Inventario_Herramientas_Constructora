package com.constructora.inventario.controller;

import com.constructora.inventario.model.Inventario;
import com.constructora.inventario.service.InventarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventarios")
public class InventarioController {
    private final InventarioService service;

    public InventarioController(InventarioService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Inventario> crear(@RequestBody Inventario nuevoInventario) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(nuevoInventario));
    }

    @GetMapping
    public List<Inventario> listar() {
        return service.listar();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Inventario> actualizar(@PathVariable Long id, @RequestBody Inventario inventario) {
        return ResponseEntity.ok(service.actualizar(id, inventario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
