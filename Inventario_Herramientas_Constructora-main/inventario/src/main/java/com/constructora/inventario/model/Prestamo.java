package com.constructora.inventario.model;

import jakarta.persistence.*;
import java.time.LocalDate;

import lombok.*;

@Setter
@Getter
@Entity
@Table(name = "prestamo")
public class Prestamo {
    // Getters y Setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "herramienta_id", nullable = false)
    private Herramienta herramienta;

    @ManyToOne
    @JoinColumn(name = "trabajador_id", nullable = false)
    private Trabajador trabajador;

    private String fechaPrestamo;
    private String fechaDevolucion;

}
