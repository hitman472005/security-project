package com.example.backend_security.controller;



import com.example.backend_security.dto.GoogleResponse;
import com.example.backend_security.entity.User;
import com.example.backend_security.security.CustomUserDetailsService;
import com.example.backend_security.security.JwtUtil;
import com.example.backend_security.service.GoogleService;
import com.example.backend_security.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Endpoints for login and Google registration")
public class AuthController {


}

