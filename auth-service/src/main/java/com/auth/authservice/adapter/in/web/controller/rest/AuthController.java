package com.auth.authservice.adapter.in.web.controller.rest;

import com.auth.authservice.adapter.in.web.request.LoginRequest;
import com.auth.authservice.application.dto.out.AuthResponse;
import com.auth.authservice.security.service.AuthService;
import com.common.shared.domain.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Servicios de inicio de sesión y generación de tokens JWT")
public class AuthController {

        private final AuthService authService;

        @Operation(summary = "Iniciar sesión", description = "Valida credenciales y retorna un JWT con los claims de email, roles y permisos.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Login exitoso", content = @Content(schema = @Schema(implementation = AuthResponse.class))),
                        @ApiResponse(responseCode = "401", description = "Credenciales incorrectas o cuenta inactiva", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        })
        @PostMapping("/login")
        public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
                AuthResponse res = authService.login(request);
                return ResponseEntity.status(HttpStatus.OK).body(res);
        }
}
