package com.constructora.inventario.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "inventario")
@Data // Lombok will generate getters, setters, toString, equals, and hashCode
@NoArgsConstructor // Constructor sin argumentos
@AllArgsConstructor // Constructor con todos los argumentos
public class Inventario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int stock;
    private int prestadas;
    private int daniadas;
}
