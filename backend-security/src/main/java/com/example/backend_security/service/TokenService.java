package com.example.backend_security.service;

import com.example.backend_security.entity.Token;
import com.example.backend_security.entity.User;
import com.example.backend_security.repository.TokenRepository;
import com.example.backend_security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class TokenService {

    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    @Autowired
    public TokenService(TokenRepository tokenRepository, UserRepository userRepository) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
    }

    // ✅ 1️⃣ Crear un token nuevo para un usuario
    public Token createToken(Long userId, String jwt) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found"));

        Token token = new Token();
        token.setUser(user);
        token.setToken(jwt);
        LocalDateTime expirationDateLocal = LocalDateTime.now().plusDays(7);
        token.setExpirationDate(expirationDateLocal);
        token.setValid("ACTIVO");
        token.setCreationDate(LocalDateTime.now());
        return tokenRepository.save(token);
    }

    // ✅ 2️⃣ Buscar un token por su valor (texto JWT)
    public Optional<Token> getTokenByValue(String jwt) {
        return tokenRepository.findByToken(jwt);
    }

    // ✅ 3️⃣ Listar todos los tokens de un usuario
    public List<Token> getTokensByUser(Long userId) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found"));

        return tokenRepository.findByUser(user);
    }

    // ✅ 4️⃣ Invalidar (marcar como no válido) un token al cerrar sesión
    public void invalidateToken(String jwt) throws Exception {
        Token token = tokenRepository.findByToken(jwt)
                .orElseThrow(() -> new Exception("Token not found"));

        token.setValid("INACTIVO");
        tokenRepository.save(token);
    }






}
