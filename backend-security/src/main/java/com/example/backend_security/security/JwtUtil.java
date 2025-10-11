package com.example.backend_security.security;

import com.example.backend_security.entity.User;


import io.jsonwebtoken.*;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.secret:${JWT_SECRET}}")
    private String SECRET_KEY;

    @Value("${jwt.expirationMs:${JWT_EXPIRATION}}")
    private long jwtExpiration;

    // 🔐 Método para obtener la clave segura
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    // ===============================
    // 🔹 2️⃣ Generar token JWT
    // ===============================
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();

        // Logs para depurar
        System.out.println("=== GENERATE TOKEN ===");
        System.out.println("Username: " + user.getUsername());
        System.out.println("Email: " + user.getEmail());

        // Usa el username si existe, de lo contrario el email
        String subject = (user.getUsername() != null && !user.getUsername().isEmpty())
                ? user.getUsername()
                : user.getEmail();

        System.out.println("Subject usado para el token: " + subject);
        System.out.println("========================");

        return createToken(claims, subject);
    }


    /*
        private String createToken(Map<String, Object> claims, String subject) {
            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(subject)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                    .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                    .compact();
        }*/
    private String createToken(Map<String, Object> claims, String subject) {
        LocalDateTime now = LocalDateTime.now();
        Date issuedAt = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
        Date expiration = Date.from(now.plusSeconds(jwtExpiration / 1000).atZone(ZoneId.systemDefault()).toInstant());

        System.out.println("🟢 Generando token para: " + subject);
        System.out.println("🟢 Clave secreta: " + SECRET_KEY);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // <── Aquí se usa
                .compact();
    }


    // ===============================
    // 🔹 3️⃣ Extraer datos del token
    // ===============================
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey()) // 👈 También aquí
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // ===============================
    // 🔹 4️⃣ Validar token
    // ===============================
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }



}