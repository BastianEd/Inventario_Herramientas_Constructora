package com.constructora.inventario.controller;

import com.constructora.inventario.assembler.HerramientaModelAssembler;
import com.constructora.inventario.model.Herramienta;
import com.constructora.inventario.service.HerramientaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v2/herramientas")
@Tag(name = "Herramienta V2", description = "API HATEOAS para la gestión de herramientas")
public class HerramientaV2Controller {
    private final HerramientaService service;
    private final HerramientaModelAssembler assembler;

    public HerramientaV2Controller(HerramientaService service, HerramientaModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @Operation(summary = "Listar todas las herramientas",
            description = "Obtiene una lista con todas las herramientas registradas en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de herramientas obtenida exitosamente")
    })
    @GetMapping
    public CollectionModel<EntityModel<Herramienta>> listar() {
        List<EntityModel<Herramienta>> herramientas = service.listar().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(herramientas,
                linkTo(methodOn(HerramientaV2Controller.class).listar()).withSelfRel());
    }

    @Operation(summary = "Obtener una herramienta por ID",
            description = "Obtiene una herramienta específica por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Herramienta encontrada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Herramienta no encontrada")
    })
    @GetMapping("/{id}")
    public EntityModel<Herramienta> obtenerPorId(@Parameter(description = "ID de la herramienta a obtener")
                                                @PathVariable Long id) {
        Herramienta herramienta = service.listar().stream()
                .filter(h -> h.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Herramienta no encontrada"));

        return assembler.toModel(herramienta);
    }

    @Operation(summary = "Crear una nueva herramienta",
            description = "Crea una herramienta y la almacena en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Herramienta creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    @PostMapping
    public ResponseEntity<EntityModel<Herramienta>> crear(@RequestBody Herramienta nuevaHerramienta) {
        Herramienta herramienta = service.guardar(nuevaHerramienta);
        EntityModel<Herramienta> entityModel = assembler.toModel(herramienta);
        
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @Operation(summary = "Actualizar los datos de una herramienta",
            description = "Permite actualizar la información de una herramienta específica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Herramienta actualizada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Herramienta no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Herramienta>> actualizar(
            @Parameter(description = "ID de la herramienta a actualizar") @PathVariable Long id,
            @RequestBody Herramienta herramienta) {
        
        Herramienta herramientaActualizada = service.actualizar(id, herramienta);
        EntityModel<Herramienta> entityModel = assembler.toModel(herramientaActualizada);
        
        return ResponseEntity.ok(entityModel);
    }

    @Operation(summary = "Prestar una herramienta",
            description = "Marca una herramienta como prestada, reduciendo su cantidad disponible.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Herramienta prestada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Herramienta no encontrada"),
            @ApiResponse(responseCode = "400", description = "No hay herramientas disponibles para prestar")
    })
    @PutMapping("/{id}/prestar")
    public ResponseEntity<EntityModel<Herramienta>> prestarHerramienta(
            @Parameter(description = "ID de la herramienta a prestar") @PathVariable Long id) {
        
        service.prestarHerramienta(id);
        
        Herramienta herramienta = service.listar().stream()
                .filter(h -> h.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Herramienta no encontrada"));
        
        return ResponseEntity.ok(assembler.toModel(herramienta));
    }

    @Operation(summary = "Devolver una herramienta",
            description = "Marca una herramienta como devuelta, aumentando su cantidad disponible.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Herramienta devuelta exitosamente"),
            @ApiResponse(responseCode = "404", description = "Herramienta no encontrada"),
            @ApiResponse(responseCode = "400", description = "No hay herramientas prestadas para devolver")
    })
    @PutMapping("/{id}/devolver")
    public ResponseEntity<EntityModel<Herramienta>> devolverHerramienta(
            @Parameter(description = "ID de la herramienta a devolver") @PathVariable Long id) {
        
        service.devolverHerramienta(id);
        
        Herramienta herramienta = service.listar().stream()
                .filter(h -> h.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Herramienta no encontrada"));
        
        return ResponseEntity.ok(assembler.toModel(herramienta));
    }

    @Operation(summary = "Eliminar una herramienta",
            description = "Permite eliminar una herramienta del sistema usando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Herramienta eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Herramienta no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@Parameter(description = "ID de la herramienta a eliminar")
                                        @PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}