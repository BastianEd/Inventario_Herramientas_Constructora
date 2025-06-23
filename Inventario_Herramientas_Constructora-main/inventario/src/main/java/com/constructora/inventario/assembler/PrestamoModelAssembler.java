package com.constructora.inventario.assembler;

import com.constructora.inventario.controller.PrestamoV2Controller;
import com.constructora.inventario.model.Prestamo;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PrestamoModelAssembler implements RepresentationModelAssembler<Prestamo, EntityModel<Prestamo>> {

    @Override
    public EntityModel<Prestamo> toModel(Prestamo prestamo) {
        EntityModel<Prestamo> prestamoModel = EntityModel.of(prestamo,
                linkTo(methodOn(PrestamoV2Controller.class).obtenerPorId(prestamo.getId())).withSelfRel(),
                linkTo(methodOn(PrestamoV2Controller.class).listar()).withRel("prestamos")
        );
        
        // Agregar enlace para devolver el préstamo si no tiene fecha de devolución
        if (prestamo.getFechaDevolucion() == null || prestamo.getFechaDevolucion().isEmpty()) {
            prestamoModel.add(linkTo(methodOn(PrestamoV2Controller.class).devolverPrestamo(prestamo.getId())).withRel("devolver"));
        }
        
        return prestamoModel;
    }
}