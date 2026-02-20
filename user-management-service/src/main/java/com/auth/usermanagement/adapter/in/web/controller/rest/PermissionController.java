package com.auth.usermanagement.adapter.in.web.controller.rest;

import com.auth.usermanagement.adapter.in.web.request.CreatePermissionRequest;
import com.auth.usermanagement.adapter.in.web.request.UpdatePermissionRequest;
import com.auth.usermanagement.application.dto.out.PermissionResponse;
import com.auth.usermanagement.domain.port.in.PermissionUseCasePort;
import com.common.shared.application.dto.AuditMessage;
import com.common.shared.application.dto.PaginatedResponse;
import com.common.shared.domain.exception.ErrorResponse;
import com.common.shared.infrastructure.messaging.MessagePublisher;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
@Tag(name = "Permisos", description = "Operaciones para la gestión de permisos individuales")
public class PermissionController {

    private final PermissionUseCasePort permissionUseCasePort;
    private final MessagePublisher messagePublisher;

    @Operation(summary = "Crear nuevo permiso", description = "Registra un permiso y lo vincula a un módulo existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Permiso creado"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Módulo no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<PermissionResponse> create(@Valid @RequestBody CreatePermissionRequest request) {
        PermissionResponse created = permissionUseCasePort.create(request);
        AuditMessage message = new AuditMessage(
                "user-management-service",
                "PERMISSION_CREATED",
                created.id().toString(),
                "Permission created: " + created.name(),
                LocalDateTime.now(),
                "ACTIVE",
                "traceId");
        messagePublisher.sendAuditLog(message);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Actualizar permiso")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Permiso actualizado", content = @Content(schema = @Schema(implementation = PermissionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Permiso no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<PermissionResponse> update(@PathVariable UUID id,
            @Valid @RequestBody UpdatePermissionRequest request) {
        PermissionResponse updated = permissionUseCasePort.update(id, request);
        AuditMessage message = new AuditMessage(
                "user-management-service",
                "PERMISSION_UPDATED",
                updated.id().toString(),
                "Permission updated: " + updated.name(),
                LocalDateTime.now(),
                "ACTIVE",
                "traceId");
        messagePublisher.sendAuditLog(message);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Desactivar permiso")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Permiso desactivado"),
            @ApiResponse(responseCode = "404", description = "Permiso no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable UUID id) {
        permissionUseCasePort.deactivate(id);
        AuditMessage message = new AuditMessage(
                "user-management-service",
                "PERMISSION_DEACTIVATED",
                id.toString(),
                "Permission deactivated: " + id,
                LocalDateTime.now(),
                "ACTIVE",
                "traceId");
        messagePublisher.sendAuditLog(message);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Activar permiso")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Permiso activado"),
            @ApiResponse(responseCode = "404", description = "Permiso no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activate(@PathVariable UUID id) {
        permissionUseCasePort.activate(id);
        AuditMessage message = new AuditMessage(
                "user-management-service",
                "PERMISSION_ACTIVATED",
                id.toString(),
                "Permission activated: " + id,
                LocalDateTime.now(),
                "ACTIVE",
                "traceId");
        messagePublisher.sendAuditLog(message);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Buscar permiso por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Permiso encontrado", content = @Content(schema = @Schema(implementation = PermissionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Permiso no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<PermissionResponse> findById(@PathVariable UUID id) {
        Optional<PermissionResponse> opt = permissionUseCasePort.findById(id);
        return opt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Listar todos los permisos registrados")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado devuelto"),
            @ApiResponse(responseCode = "500", description = "Error interno", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<PermissionResponse>> findAll() {
        List<PermissionResponse> list = permissionUseCasePort.findAll();
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "Búsqueda paginada de permisos", description = "Filtra permisos por coincidencia parcial en el nombre.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Resultados"),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/search")
    public ResponseEntity<PaginatedResponse<PermissionResponse>> search(
            @Parameter(description = "Nombre o parte del nombre del permiso") @RequestParam(required = false) String name,
            @Parameter(description = "Número de página (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Elementos por página") @RequestParam(defaultValue = "10") int size) {

        PaginatedResponse<PermissionResponse> result = permissionUseCasePort.search(name, page, size);
        return ResponseEntity.ok(result);
    }
}
