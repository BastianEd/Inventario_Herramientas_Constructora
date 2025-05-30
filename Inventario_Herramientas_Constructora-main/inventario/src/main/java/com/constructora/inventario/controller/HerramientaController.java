package com.constructora.inventario.controller;

import com.constructora.inventario.model.Herramienta;
import com.constructora.inventario.service.HerramientaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/herramientas")
public class HerramientaController {
    private final HerramientaService service;

    public HerramientaController(HerramientaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Herramienta> crear(@RequestBody Herramienta nuevaHerramienta) {
        Herramienta herramientaCreada = service.guardar(nuevaHerramienta);
        return ResponseEntity.status(HttpStatus.CREATED).body(herramientaCreada);
    }

    @GetMapping
    public List<Herramienta> listar() {
        return service.listar();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Herramienta> actualizar(@PathVariable Long id, @RequestBody Herramienta herramienta) {
        return ResponseEntity.ok(service.actualizar(id, herramienta));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/prestar")
    public ResponseEntity<Void> prestar(@PathVariable Long id) {
        service.prestarHerramienta(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/devolver")
    public ResponseEntity<Void> devolver(@PathVariable Long id) {
        service.devolverHerramienta(id);
        return ResponseEntity.ok().build();
    }
}
