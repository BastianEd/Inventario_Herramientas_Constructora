package com.constructora.inventario.controller;

import com.constructora.inventario.model.Trabajador;
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
    public ResponseEntity<Trabajador> crear(@RequestBody Trabajador nuevoTrabajador) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(nuevoTrabajador));
    }

    @GetMapping
    public List<Trabajador> listar() {
        return service.listar();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Trabajador> actualizar(@PathVariable Long id, @RequestBody Trabajador trabajador) {
        return ResponseEntity.ok(service.actualizar(id, trabajador));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
