package com.common.shared.security.filter;

import com.common.shared.security.service.AuthPrincipal;
import com.common.shared.security.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        String jwt = null;
        String username = null;

        System.out.println("Procesando request: " + request.getRequestURI());

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7).trim();
            System.out.println("Token recibido: " + jwt.substring(0, Math.min(10, jwt.length())) + "...");
        } else {
            System.out.println("No Authorization header or not Bearer token");
        }

        if (jwt != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                username = jwtUtil.extractUsername(jwt);
                System.out.println("Username extraído: " + username);

                if (username != null && jwtUtil.validateToken(jwt)) {
                    Claims claims = jwtUtil.extractAllClaims(jwt);

                    Object rolesObj = claims.get("roles");
                    List<String> roles = new ArrayList<>();
                    if (rolesObj instanceof List) {
                        roles = ((List<?>) rolesObj).stream()
                                .filter(Objects::nonNull)
                                .map(Object::toString)
                                .collect(Collectors.toList());
                    }

                    Object permsObj = claims.get("permissions");
                    List<String> permissions = new ArrayList<>();
                    if (permsObj instanceof List) {
                        permissions = ((List<?>) permsObj).stream()
                                .filter(Objects::nonNull)
                                .map(Object::toString)
                                .collect(Collectors.toList());
                    }

                    Set<String> normalized = new java.util.HashSet<>();
                    roles.forEach(r -> normalized.add(r.startsWith("ROLE_") ? r : "ROLE_" + r));
                    permissions.forEach(p -> normalized.add(p.startsWith("PERM_") ? p : "PERM_" + p));

                    Collection<GrantedAuthority> authorities = new ArrayList<>();
                    normalized.forEach(a -> authorities.add(new SimpleGrantedAuthority(a)));

                    String userIdClaim = null;
                    try {
                        Object uid = claims.get("userId");
                        userIdClaim = uid == null ? null : uid.toString();
                    } catch (Exception ignored) {
                    }

                    AuthPrincipal principal = new AuthPrincipal(username, userIdClaim);

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            principal, null, authorities);
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    System.out.println("Autenticación exitosa para: " + username);
                } else {
                    System.out.println("Token inválido o username nulo");
                }
            } catch (Exception e) {
                System.out.println("Error procesando token: " + e.getMessage());
                e.printStackTrace();
            }
        }

        filterChain.doFilter(request, response);
    }
}
