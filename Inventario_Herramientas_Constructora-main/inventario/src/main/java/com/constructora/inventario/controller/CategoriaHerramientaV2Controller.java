package com.constructora.inventario.controller;

import com.constructora.inventario.assembler.CategoriaHerramientaModelAssembler;
import com.constructora.inventario.model.CategoriaHerramienta;
import com.constructora.inventario.service.CategoriaHerramientaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v2/categorias")
@Tag(name = "Categoría Herramienta V2", description = "API HATEOAS para la gestión de categorías de herramientas")
public class CategoriaHerramientaV2Controller {
    private final CategoriaHerramientaService service;
    private final CategoriaHerramientaModelAssembler assembler;

    public CategoriaHerramientaV2Controller(CategoriaHerramientaService service, CategoriaHerramientaModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @Operation(summary = "Listar todas las categorías de herramientas",
            description = "Obtiene una lista con todas las categorías de herramientas registradas en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de categorías obtenida exitosamente")
    })
    @GetMapping
    public CollectionModel<EntityModel<CategoriaHerramienta>> listar() {
        List<EntityModel<CategoriaHerramienta>> categorias = service.listar().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(categorias,
                linkTo(methodOn(CategoriaHerramientaV2Controller.class).listar()).withSelfRel());
    }

    @Operation(summary = "Obtener una categoría de herramienta por ID",
            description = "Obtiene una categoría de herramienta específica por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría encontrada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @GetMapping("/{id}")
    public EntityModel<CategoriaHerramienta> obtenerPorId(@Parameter(description = "ID de la categoría a obtener")
                                                         @PathVariable Long id) {
        CategoriaHerramienta categoria = service.obtenerPorId(id);
        return assembler.toModel(categoria);
    }

    @Operation(summary = "Crear una nueva categoría de herramienta",
            description = "Crea una categoría de herramienta y la almacena en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Categoría creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    @PostMapping
    public ResponseEntity<EntityModel<CategoriaHerramienta>> crear(@RequestBody CategoriaHerramienta nuevaCategoria) {
        CategoriaHerramienta categoria = service.guardar(nuevaCategoria);
        EntityModel<CategoriaHerramienta> entityModel = assembler.toModel(categoria);
        
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @Operation(summary = "Eliminar una categoría de herramienta",
            description = "Permite eliminar una categoría de herramienta del sistema usando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Categoría eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@Parameter(description = "ID de la categoría a eliminar")
                                        @PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}