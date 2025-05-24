package com.constructora.inventario.model;

import jakarta.persistence.*;

@Entity
@Table(name = "categoria_herramienta")
public class CategoriaHerramienta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipo;

    private String marca;

    private int anioAntiguedad;

    public CategoriaHerramienta() {
    }

    public CategoriaHerramienta(String tipo, String marca, int anioAntiguedad) {
        this.tipo = tipo;
        this.marca = marca;
        this.anioAntiguedad = anioAntiguedad;
    }

    public Long getId() {
        return id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public int getAnioAntiguedad() {
        return anioAntiguedad;
    }

    public void setAnioAntiguedad(int anioAntiguedad) {
        this.anioAntiguedad = anioAntiguedad;
    }
}
