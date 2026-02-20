package com.auth.gatewayservice.filter;

import com.common.shared.security.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 100)
@Slf4j
@RequiredArgsConstructor
public class AuthenticationFilter implements Filter {

    private final JwtUtil jwtUtil;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    // Endpoints públicos que no requieren token
    private static final List<String> EXCLUDED_PATHS = Arrays.asList(
            "/api/auth/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html");

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String path = request.getRequestURI();

        // Check if path is excluded
        boolean isExcluded = EXCLUDED_PATHS.stream().anyMatch(p -> pathMatcher.match(p, path));

        if (isExcluded) {
            chain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Solicitud no autenticada a {}: falta header Authorization", path);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Missing or invalid Authorization header");
            return;
        }

        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token)) {
            log.warn("Token inválido para solicitud a {}", path);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid Token");
            return;
        }

        try {
            Claims claims = jwtUtil.extractAllClaims(token);
            String userId = claims.get("userId", String.class);
            Object rolesObj = claims.get("roles");
            String roles = rolesObj != null ? rolesObj.toString() : "";

            MutableHttpServletRequest mutableRequest = new MutableHttpServletRequest(request);
            if (userId != null) {
                mutableRequest.putHeader("X-Auth-User-Id", userId);
            }
            mutableRequest.putHeader("X-Auth-Roles", roles);
            // También propagar el email/subject si es necesario
            mutableRequest.putHeader("X-Auth-Email", claims.getSubject());

            chain.doFilter(mutableRequest, response);

        } catch (Exception e) {
            log.error("Error procesando token en gateway", e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Error processing token");
        }
    }
}
