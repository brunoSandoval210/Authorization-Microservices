package com.auth.usermanagement.adapter.in.web.controller.internal;

import com.common.shared.application.dto.UserSecurityResponse;
import com.auth.usermanagement.domain.port.in.GetUserForAuthUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/users")
@RequiredArgsConstructor
@Tag(name = "API de Usuarios Internos", description = "Endpoints internos para comunicación entre microservicios")
public class InternalUserController {

    private final GetUserForAuthUseCase getUserForAuthUseCase;

    @Operation(summary = "Usuario para autenticación", description = "Usado por auth-service para validar credenciales")
    @GetMapping("/search/findByUsername")
    public ResponseEntity<UserSecurityResponse> findByUsername(@RequestParam String username) {
        return ResponseEntity.ok(getUserForAuthUseCase.execute(username));
    }
}
