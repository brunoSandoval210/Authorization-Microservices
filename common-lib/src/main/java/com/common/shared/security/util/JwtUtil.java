package com.common.shared.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.common.shared.application.dto.UserSecurityResponse;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final Key key;

    private final long expirationMs;

    public JwtUtil(
            @Value("${jwt.secret:default_change_this_secret_2026_please_make_it_long_enough_32123}") String secret,
            @Value("${jwt.expiration-ms:3600000}") long expirationMs) {
        // Validación clara si la clave es insuficiente para HMAC
        if (secret == null || secret.getBytes(StandardCharsets.UTF_8).length < 32) {
            throw new IllegalStateException("La propiedad 'jwt.secret' debe existir y tener al menos 32 bytes. " +
                    "Usa una variable de entorno o define 'jwt.secret' en application.yml");
        }
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
    }

    public String generateToken(UserSecurityResponse user) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .subject(user.email())
                .claim("roles", user.roles())
                .claim("permissions", user.permissions())
                .claim("userId", user.userId())
                .claim("email", user.email())
                .issuedAt(now)
                .expiration(exp)
                .signWith(key)
                .compact();
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith((javax.crypto.SecretKey) key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}