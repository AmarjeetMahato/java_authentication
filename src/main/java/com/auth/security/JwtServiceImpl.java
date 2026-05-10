package com.auth.security;

import com.auth.domain.Users.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements IJwtService {
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access.expiration}")
    private long accessExpiration; // e.g. 1 hour

    @Value("${jwt.refresh.expiration}")
    private long refreshExpiration; // e.g. 7 days


    // =========================
    // ACCESS TOKEN
    // =========================
    @Override
    public String generateAccessToken(User user) {
        return generateToken(user, accessExpiration);
    }

    // =========================
    // REFRESH TOKEN
    // =========================
    @Override
    public String generateRefreshToken(User user) {
        return generateToken(user, refreshExpiration);
    }

    // =========================
    // CORE TOKEN GENERATION
    // =========================
    private String generateToken(User user, long expiration) {

        Map<String, Object> claims = new HashMap<>();

        claims.put("userId", user.getId());
        claims.put("emailVerified", user.isEmailVerified());
        claims.put("active", user.isActive());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // =========================
    // EXTRACT EMAIL
    // =========================
    @Override
    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    // =========================
    // VALIDATION
    // =========================
    @Override
    public boolean isTokenValid(String token, User user) {

        String email = extractEmail(token);

        return (email.equals(user.getEmail())
                && !isTokenExpired(token));
    }

    // =========================
    // CHECK EXPIRATION
    // =========================
    private boolean isTokenExpired(String token) {
        return extractAllClaims(token)
                .getExpiration()
                .before(new Date());
    }

    // =========================
    // PARSE CLAIMS
    // =========================
    private Claims extractAllClaims(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // =========================
    // SIGNING KEY
    // =========================
    private Key getSigningKey() {

        return Keys.hmacShaKeyFor(
                secretKey.getBytes()
        );
    }
}
