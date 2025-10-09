package com.example.backend_security.service;

import com.example.backend_security.entity.Token;
import com.example.backend_security.entity.User;
import com.example.backend_security.repository.TokenRepository;
import com.example.backend_security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    // Crear token para un usuario
    public Token createToken(Long userId, Token token) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found"));

        token.setUser(user);
        return tokenRepository.save(token);
    }

    // Obtener token por ID
    public Optional<Token> getTokenById(Long id) {
        return tokenRepository.findById(id);
    }

    // Obtener todos los tokens de un usuario
    public List<Token> getTokensByUser(Long userId) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found"));

        return tokenRepository.findByUser(user);
    }

    // Eliminar token
    public void deleteToken(Long id) throws Exception {
        Token token = tokenRepository.findById(id)
                .orElseThrow(() -> new Exception("Token not found"));
        tokenRepository.delete(token);
    }
}