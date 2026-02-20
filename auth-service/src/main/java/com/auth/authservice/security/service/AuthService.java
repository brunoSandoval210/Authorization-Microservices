package com.auth.authservice.security.service;

import com.auth.authservice.adapter.in.web.request.LoginRequest;
import com.auth.authservice.application.dto.out.AuthResponse;
import com.auth.authservice.domain.port.in.GetUserForAuthUseCase;

import com.common.shared.application.dto.UserSecurityResponse;
import com.common.shared.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final JwtUtil jwtUtil;
    private final GetUserForAuthUseCase getUserForAuthUseCase;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse login(LoginRequest request) {
        String email = request.email() == null ? "" : request.email().trim().toLowerCase();
        log.debug("Intento de inicio de sesión para email={}", email);

        UserSecurityResponse user;
        try {
            user = getUserForAuthUseCase.execute(email);
        } catch (Exception e) {
            log.warn("Inicio de sesión fallido: usuario no encontrado o error de servicio para email={}", email, e);
            throw new BadCredentialsException("Credenciales erróneas");
        }

        if (user == null) {
            log.warn("Inicio de sesión fallido: usuario no encontrado para email={}", email);
            throw new BadCredentialsException("Credenciales erróneas");
        }

        String stored = user.password();
        if (stored == null) {
            log.warn("Inicio de sesión fallido: el usuario {} no tiene contraseña almacenada", email);
            throw new BadCredentialsException("Credenciales erróneas");
        }

        boolean matches = passwordEncoder.matches(request.password(), stored);
        log.debug("¿Coincide la contraseña para {}? {}", email, matches);

        if (!matches) {
            log.warn("Inicio de sesión fallido: contraseña incorrecta para email={}", email);
            throw new BadCredentialsException("Credenciales erróneas");
        }

        // comprobar estado de la cuenta
        if (!user.enabled()) {
            log.warn("Inicio de sesión fallido: cuenta deshabilitada para email={}", email);
            throw new DisabledException("Cuenta deshabilitada");
        }

        // roles and permissions are already strings in UserSecurityResponse
        List<String> roles = user.roles();
        List<String> permissions = user.permissions();

        String token = jwtUtil.generateToken(new UserSecurityResponse(
                user.userId(),
                user.email(),
                user.password(),
                user.enabled(),
                roles,
                permissions
        ));
        log.info("Inicio de sesión exitoso para email={}", email);
        return new AuthResponse(token);
    }
}
