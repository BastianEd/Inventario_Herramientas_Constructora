package com.constructora.inventario.assembler;

import com.constructora.inventario.controller.InventarioV2Controller;
import com.constructora.inventario.model.Inventario;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class InventarioModelAssembler implements RepresentationModelAssembler<Inventario, EntityModel<Inventario>> {

    @Override
    public EntityModel<Inventario> toModel(Inventario inventario) {
        return EntityModel.of(inventario,
                linkTo(methodOn(InventarioV2Controller.class).obtenerPorId(inventario.getId())).withSelfRel(),
                linkTo(methodOn(InventarioV2Controller.class).listar()).withRel("inventarios")
        );
    }
}