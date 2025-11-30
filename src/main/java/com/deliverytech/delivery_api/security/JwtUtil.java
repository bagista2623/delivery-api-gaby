package com.deliverytech.delivery_api.security;

import com.deliverytech.delivery_api.entity.Usuario;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private final String SECRET_KEY = "MINHA_CHAVE_SECRETA_DE_256_BITS_123456789012345678901234567890";
    private final long EXPIRATION_TIME = 1000 * 60 * 60; // 1h

    // -------------------------------
    // GERAR TOKEN
    // -------------------------------
    public String generateToken(Usuario usuario) {
        return Jwts.builder()
                .setSubject(usuario.getEmail())
                .claim("id", usuario.getId())
                .claim("role", usuario.getRole().name())
                .claim("restauranteId", usuario.getRestauranteId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .compact();
    }

    // -------------------------------
    // EXTRAI USERNAME DO TOKEN
    // -------------------------------
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // -------------------------------
    // EXTRAI EXPIRAÇÃO
    // -------------------------------
    public long getExpirationFromToken(String token) {
        return extractAllClaims(token).getExpiration().getTime();
    }

    // -------------------------------
    // VALIDA TOKEN
    // -------------------------------
    public boolean validateToken(String token, Usuario usuario) {
        String username = extractUsername(token);
        return username.equals(usuario.getEmail()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    // -------------------------------
    // CLAIMS
    // -------------------------------
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
