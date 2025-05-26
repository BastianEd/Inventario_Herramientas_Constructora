package com.constructora.inventario.model;

import jakarta.persistence.*;

@Entity
@Table(name = "inventario")
public class Inventario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int stock;
    private int prestadas;
    private int daniadas;

    public Inventario() {
    }

    public Inventario(int stock, int prestadas, int daniadas) {
        this.stock = stock;
        this.prestadas = prestadas;
        this.daniadas = daniadas;
    }

    public Long getId() {
        return id;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getPrestadas() {
        return prestadas;
    }

    public void setPrestadas(int prestadas) {
        this.prestadas = prestadas;
    }

    public int getDaniadas() {
        return daniadas;
    }

    public void setDaniadas(int daniadas) {
        this.daniadas = daniadas;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
