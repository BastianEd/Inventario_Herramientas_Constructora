package com.constructora.inventario.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categoria_herramienta")
@Data // Lombok will generate getters, setters, toString, equals, and hashCode
@NoArgsConstructor // Constructor sin argumentos
@AllArgsConstructor // Constructor con todos los argumentos
public class CategoriaHerramienta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipo;
    private String marca;
    private int anioAntiguedad;
}
