package com.constructora.inventario.assembler;

import com.constructora.inventario.controller.CategoriaHerramientaV2Controller;
import com.constructora.inventario.model.CategoriaHerramienta;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CategoriaHerramientaModelAssembler implements RepresentationModelAssembler<CategoriaHerramienta, EntityModel<CategoriaHerramienta>> {

    @Override
    public EntityModel<CategoriaHerramienta> toModel(CategoriaHerramienta categoriaHerramienta) {
        return EntityModel.of(categoriaHerramienta,
                linkTo(methodOn(CategoriaHerramientaV2Controller.class).obtenerPorId(categoriaHerramienta.getId())).withSelfRel(),
                linkTo(methodOn(CategoriaHerramientaV2Controller.class).listar()).withRel("categorias")
        );
    }
}