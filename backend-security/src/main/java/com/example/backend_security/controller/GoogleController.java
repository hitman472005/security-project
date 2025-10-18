package com.example.backend_security.controller;

import com.example.backend_security.dto.GoogleResponse;
import com.example.backend_security.entity.User;
import com.example.backend_security.security.JwtUtil;
import com.example.backend_security.service.GoogleService;
import com.example.backend_security.service.TokenService;
import com.example.backend_security.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Map;

@RestController
@RequestMapping("/auth/google")
@Tag(name = "Authentication with Google")
public class GoogleController {


    private final GoogleService googleService;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final TokenService tokenService;

    public GoogleController(GoogleService googleService, UserService userService, JwtUtil jwtUtil, TokenService tokenService) {
        this.googleService = googleService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.tokenService = tokenService;
    }

    @PostMapping("/loginWithGoogle")
    public ResponseEntity<?> loginWithGoogle(@RequestBody Map<String, String> body) {
        try {
            String code = body.get("code");


            // 1️⃣ Intercambiar code por token en Google
            GoogleResponse tokenResponse = googleService.exchangeCodeForToken(code);
            // 2️⃣ Obtener información del usuario
            Map<String, Object> userInfo = googleService.getUserInfo(tokenResponse.getAccess_token());
            String name = (String) userInfo.get("name");


            // 3️⃣ Registrar/actualizar en MySQL
            User user = userService.registerOrUpdateOAuthUser(userInfo);
            // 4️⃣ Generar JWT propio
            String jwt = jwtUtil.generateToken(user);
            // 5️⃣ Guardar token en base de datos
            tokenService.createToken(user.getId(), jwt);

            System.out.println(jwt);
            return ResponseEntity.ok(Map.of(
                    "token", jwt,
                    "email", user.getEmail(),
                    "name", user.getName(),
                    "picture", user.getPhotoUrl()
            ));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;

    @GetMapping("/login-url")
    public Map<String, String> getLoginUrl() {
        String scope = "openid email profile";
        String responseType = "code";

        String url = "https://accounts.google.com/o/oauth2/v2/auth"
                + "?client_id=" + clientId
                + "&redirect_uri=" + redirectUri
                + "&response_type=" + responseType
                + "&scope=" + scope;

        return Map.of("url", url);
    }

}
