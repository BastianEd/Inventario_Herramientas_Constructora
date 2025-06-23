package com.constructora.inventario.controller;

import com.constructora.inventario.assembler.InventarioModelAssembler;
import com.constructora.inventario.model.Inventario;
import com.constructora.inventario.service.InventarioService;
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
@RequestMapping("/api/v2/inventarios")
@Tag(name = "Inventario V2", description = "API HATEOAS para la gestión de inventarios")
public class InventarioV2Controller {
    private final InventarioService service;
    private final InventarioModelAssembler assembler;

    public InventarioV2Controller(InventarioService service, InventarioModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @Operation(summary = "Listar todos los inventarios",
            description = "Obtiene una lista con todos los inventarios registrados en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de inventarios obtenida exitosamente")
    })
    @GetMapping
    public CollectionModel<EntityModel<Inventario>> listar() {
        List<EntityModel<Inventario>> inventarios = service.listar().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(inventarios,
                linkTo(methodOn(InventarioV2Controller.class).listar()).withSelfRel());
    }

    @Operation(summary = "Obtener un inventario por ID",
            description = "Obtiene un inventario específico por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventario encontrado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Inventario no encontrado")
    })
    @GetMapping("/{id}")
    public EntityModel<Inventario> obtenerPorId(@Parameter(description = "ID del inventario a obtener")
                                               @PathVariable Long id) {
        Inventario inventario = service.listar().stream()
                .filter(i -> i.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado"));

        return assembler.toModel(inventario);
    }

    @Operation(summary = "Crear un nuevo inventario",
            description = "Crea un inventario y lo almacena en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Inventario creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    @PostMapping
    public ResponseEntity<EntityModel<Inventario>> crear(@RequestBody Inventario nuevoInventario) {
        Inventario inventario = service.guardar(nuevoInventario);
        EntityModel<Inventario> entityModel = assembler.toModel(inventario);
        
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @Operation(summary = "Actualizar los datos de un inventario",
            description = "Permite actualizar la información de un inventario específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventario actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Inventario no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Inventario>> actualizar(
            @Parameter(description = "ID del inventario a actualizar") @PathVariable Long id,
            @RequestBody Inventario inventario) {
        
        Inventario inventarioActualizado = service.actualizar(id, inventario);
        EntityModel<Inventario> entityModel = assembler.toModel(inventarioActualizado);
        
        return ResponseEntity.ok(entityModel);
    }

    @Operation(summary = "Eliminar un inventario",
            description = "Permite eliminar un inventario del sistema usando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Inventario eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Inventario no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@Parameter(description = "ID del inventario a eliminar")
                                        @PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}