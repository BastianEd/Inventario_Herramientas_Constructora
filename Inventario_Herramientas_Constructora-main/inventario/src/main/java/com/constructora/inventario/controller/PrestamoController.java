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
    public ResponseEntity<Prestamo> crearPrestamo(@RequestParam Long herramientaId, @RequestParam Long trabajadorId) {
        Prestamo prestamoCreado = service.crearPrestamo(herramientaId, trabajadorId);
        return ResponseEntity.status(HttpStatus.CREATED).body(prestamoCreado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Prestamo> actualizarPrestamo(@PathVariable Long id, @RequestBody Prestamo prestamo) {
        Prestamo prestamoActualizado = service.actualizarPrestamo(id, prestamo);
        return ResponseEntity.ok(prestamoActualizado);
    }




    @PostMapping("/{id}/devolver")
    public ResponseEntity<Void> devolverPrestamo(@PathVariable Long id) {
        service.devolverPrestamo(id);
        return ResponseEntity.ok().build();
    }


    @GetMapping
    public List<Prestamo> listarPrestamos() {
        return service.listarPrestamos();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPrestamo(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
