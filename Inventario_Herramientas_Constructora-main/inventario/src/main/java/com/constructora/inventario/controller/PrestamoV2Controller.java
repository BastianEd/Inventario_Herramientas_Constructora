package com.constructora.inventario.controller;

import com.constructora.inventario.assembler.PrestamoModelAssembler;
import com.constructora.inventario.model.Prestamo;
import com.constructora.inventario.service.PrestamoService;
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
@RequestMapping("/api/v2/prestamos")
@Tag(name = "Préstamo V2", description = "API HATEOAS para la gestión de préstamos de herramientas")
public class PrestamoV2Controller {
    private final PrestamoService service;
    private final PrestamoModelAssembler assembler;

    public PrestamoV2Controller(PrestamoService service, PrestamoModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @Operation(summary = "Listar todos los préstamos",
            description = "Obtiene una lista con todos los préstamos registrados en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de préstamos obtenida exitosamente")
    })
    @GetMapping
    public CollectionModel<EntityModel<Prestamo>> listar() {
        List<EntityModel<Prestamo>> prestamos = service.listarPrestamos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(prestamos,
                linkTo(methodOn(PrestamoV2Controller.class).listar()).withSelfRel());
    }

    @Operation(summary = "Obtener un préstamo por ID",
            description = "Obtiene un préstamo específico por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Préstamo encontrado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Préstamo no encontrado")
    })
    @GetMapping("/{id}")
    public EntityModel<Prestamo> obtenerPorId(@Parameter(description = "ID del préstamo a obtener")
                                             @PathVariable Long id) {
        Prestamo prestamo = service.listarPrestamos().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Préstamo no encontrado"));

        return assembler.toModel(prestamo);
    }

    @Operation(summary = "Crear un nuevo préstamo",
            description = "Registra un préstamo de herramienta a un trabajador.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Préstamo creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "404", description = "Herramienta o trabajador no encontrado")
    })
    @PostMapping("/herramienta/{herramientaId}/trabajador/{trabajadorId}")
    public ResponseEntity<EntityModel<Prestamo>> crearPrestamo(
            @Parameter(description = "ID de la herramienta a prestar") @PathVariable Long herramientaId,
            @Parameter(description = "ID del trabajador que recibe la herramienta") @PathVariable Long trabajadorId) {
        
        Prestamo prestamo = service.crearPrestamo(herramientaId, trabajadorId);
        EntityModel<Prestamo> entityModel = assembler.toModel(prestamo);
        
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @Operation(summary = "Devolver un préstamo",
            description = "Registra la devolución de una herramienta prestada.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Préstamo devuelto exitosamente"),
            @ApiResponse(responseCode = "404", description = "Préstamo no encontrado")
    })
    @PutMapping("/{id}/devolver")
    public ResponseEntity<EntityModel<Prestamo>> devolverPrestamo(
            @Parameter(description = "ID del préstamo a devolver") @PathVariable Long id) {
        
        service.devolverPrestamo(id);
        
        Prestamo prestamo = service.listarPrestamos().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Préstamo no encontrado"));
        
        return ResponseEntity.ok(assembler.toModel(prestamo));
    }

    @Operation(summary = "Eliminar un préstamo",
            description = "Permite eliminar un préstamo del sistema usando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Préstamo eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Préstamo no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@Parameter(description = "ID del préstamo a eliminar")
                                        @PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}