package com.constructora.inventario.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "herramienta")
@Data // Lombok will generate getters, setters, toString, equals, and hashCode
@NoArgsConstructor // Constructor sin argumentos
@AllArgsConstructor // Constructor con todos los argumentos
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
}
