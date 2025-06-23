package com.constructora.inventario.assembler;

import com.constructora.inventario.controller.TrabajadorV2Controller;
import com.constructora.inventario.model.Trabajador;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TrabajadorModelAssembler implements RepresentationModelAssembler<Trabajador, EntityModel<Trabajador>> {

    @Override
    public EntityModel<Trabajador> toModel(Trabajador trabajador) {
        return EntityModel.of(trabajador,
                linkTo(methodOn(TrabajadorV2Controller.class).obtenerPorId(trabajador.getId())).withSelfRel(),
                linkTo(methodOn(TrabajadorV2Controller.class).listar()).withRel("trabajadores")
        );
    }
}