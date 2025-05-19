package com.constructora.inventario.controller;

import com.constructora.inventario.model.trabajador;
import com.constructora.inventario.service.TrabajadorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/trabajadores")
public class TrabajadorController {
    private final TrabajadorService service;

    public TrabajadorController(TrabajadorService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<trabajador> crear(@RequestBody trabajador nuevoTrabajador) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(nuevoTrabajador));
    }

    @GetMapping
    public List<trabajador> listar() {
        return service.listar();
    }

    @PutMapping("/{id}")
    public ResponseEntity<trabajador> actualizar(@PathVariable Long id, @RequestBody trabajador trabajador) {
        return ResponseEntity.ok(service.actualizar(id, trabajador));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
