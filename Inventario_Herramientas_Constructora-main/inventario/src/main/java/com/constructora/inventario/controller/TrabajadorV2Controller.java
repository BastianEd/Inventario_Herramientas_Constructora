package com.constructora.inventario.controller;

import com.constructora.inventario.assembler.TrabajadorModelAssembler;
import com.constructora.inventario.model.Trabajador;
import com.constructora.inventario.service.TrabajadorService;
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
@RequestMapping("/api/v2/trabajadores")
@Tag(name = "Trabajador V2", description = "API HATEOAS para la gestión de trabajadores")
public class TrabajadorV2Controller {
    private final TrabajadorService service;
    private final TrabajadorModelAssembler assembler;

    public TrabajadorV2Controller(TrabajadorService service, TrabajadorModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @Operation(summary = "Listar todos los trabajadores",
            description = "Obtiene una lista con todos los trabajadores registrados en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de trabajadores obtenida exitosamente")
    })
    @GetMapping
    public CollectionModel<EntityModel<Trabajador>> listar() {
        List<EntityModel<Trabajador>> trabajadores = service.listar().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(trabajadores,
                linkTo(methodOn(TrabajadorV2Controller.class).listar()).withSelfRel());
    }

    @Operation(summary = "Obtener un trabajador por ID",
            description = "Obtiene un trabajador específico por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trabajador encontrado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Trabajador no encontrado")
    })
    @GetMapping("/{id}")
    public EntityModel<Trabajador> obtenerPorId(@Parameter(description = "ID del trabajador a obtener")
                                               @PathVariable Long id) {
        Trabajador trabajador = service.listar().stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Trabajador no encontrado"));

        return assembler.toModel(trabajador);
    }

    @Operation(summary = "Crear un nuevo trabajador",
            description = "Crea un trabajador y lo almacena en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Trabajador creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    @PostMapping
    public ResponseEntity<EntityModel<Trabajador>> crear(@RequestBody Trabajador nuevoTrabajador) {
        Trabajador trabajador = service.guardar(nuevoTrabajador);
        EntityModel<Trabajador> entityModel = assembler.toModel(trabajador);
        
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @Operation(summary = "Actualizar los datos de un trabajador",
            description = "Permite actualizar la información de un trabajador específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trabajador actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Trabajador no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Trabajador>> actualizar(
            @Parameter(description = "ID del trabajador a actualizar") @PathVariable Long id,
            @RequestBody Trabajador trabajador) {
        
        Trabajador trabajadorActualizado = service.actualizar(id, trabajador);
        EntityModel<Trabajador> entityModel = assembler.toModel(trabajadorActualizado);
        
        return ResponseEntity.ok(entityModel);
    }

    @Operation(summary = "Eliminar un trabajador",
            description = "Permite eliminar un trabajador del sistema usando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Trabajador eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Trabajador no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@Parameter(description = "ID del trabajador a eliminar")
                                        @PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}