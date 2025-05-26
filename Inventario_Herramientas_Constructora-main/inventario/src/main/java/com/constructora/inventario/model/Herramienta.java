package com.constructora.inventario.model;

import jakarta.persistence.*;

@Entity
@Table(name = "herramienta")
public class Herramienta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private int cantidadDisponible;
    private int cantidadPrestadas = 0; // Inicializar en 0
    private int cantidadDanadas = 0; // Cambiado a "danadas"

    @ManyToOne
    @JoinColumn(name = "categoria_id") // Nombre de la columna en la tabla herramienta
    private CategoriaHerramienta categoria;

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCantidadDisponible() {
        return cantidadDisponible;
    }

    public void setCantidadDisponible(int cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }

    public int getCantidadPrestadas() {
        return cantidadPrestadas;
    }

    public void setCantidadPrestadas(int cantidadPrestadas) {
        this.cantidadPrestadas = cantidadPrestadas;
    }

    public int getCantidadDanadas() {
        return cantidadDanadas; // Cambiado a "danadas"
    }

    public void setCantidadDanadas(int cantidadDanadas) {
        this.cantidadDanadas = cantidadDanadas; // Cambiado a "danadas"
    }

    public CategoriaHerramienta getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaHerramienta categoria) {
        this.categoria = categoria;
    }
}
