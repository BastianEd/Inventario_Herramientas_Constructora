package com.constructora.inventario.assembler;

import com.constructora.inventario.controller.HerramientaV2Controller;
import com.constructora.inventario.model.Herramienta;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class HerramientaModelAssembler implements RepresentationModelAssembler<Herramienta, EntityModel<Herramienta>> {

    @Override
    public EntityModel<Herramienta> toModel(Herramienta herramienta) {
        return EntityModel.of(herramienta,
                linkTo(methodOn(HerramientaV2Controller.class).obtenerPorId(herramienta.getId())).withSelfRel(),
                linkTo(methodOn(HerramientaV2Controller.class).listar()).withRel("herramientas"),
                linkTo(methodOn(HerramientaV2Controller.class).prestarHerramienta(herramienta.getId())).withRel("prestar"),
                linkTo(methodOn(HerramientaV2Controller.class).devolverHerramienta(herramienta.getId())).withRel("devolver")
        );
    }
}