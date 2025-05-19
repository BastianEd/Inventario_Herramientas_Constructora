package com.constructora.inventario.controller;

import com.constructora.inventario.model.Prestamo;
import com.constructora.inventario.service.PrestamoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/prestamos")
public class PrestamoController {
    private final PrestamoService service;

    public PrestamoController(PrestamoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Prestamo> crear(@RequestBody Prestamo nuevoPrestamo) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(nuevoPrestamo));
    }

    @GetMapping
    public List<Prestamo> listar() {
        return service.listar();
    }

    @PutMapping("/{id}/devolver")
    public ResponseEntity<Prestamo> devolver(@PathVariable Long id) {
        return ResponseEntity.ok(service.devolver(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
