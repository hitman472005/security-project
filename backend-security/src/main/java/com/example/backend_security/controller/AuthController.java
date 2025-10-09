package com.example.backend_security.controller;

import com.example.backend_security.service.TokenService;
import com.example.backend_security.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth") // Ruta base común
@Tag(name = "Authentication")
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) throws Exception {
        System.out.println("🟢 Entró a /logout con header: " + authHeader);
        String jwt = authHeader.replace("Bearer ", "");
       tokenService.invalidateToken(jwt);
        return ResponseEntity.ok("Logout successful. Token invalidated.");
    }

}

