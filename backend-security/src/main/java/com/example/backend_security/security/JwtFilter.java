package com.example.backend_security.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    public JwtFilter(JwtUtil jwtUtil, CustomUserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String requestTokenHeader = request.getHeader("Authorization");
        String jwtToken = null;
        String usernameOrEmail = null;

        // 🔹 1️⃣ Verificar encabezado Authorization
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                usernameOrEmail = this.jwtUtil.extractUsername(jwtToken);
                System.out.println("🟢 Token recibido para usuario/email: " + usernameOrEmail);
            } catch (Exception e) {
                System.err.println("❌ Error al extraer username/email del token: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("text/plain");
                response.getWriter().write("TOKEN INVÁLIDO O EXPIRADO");
                return;
            }
        }

        // 🔹 2️⃣ Validar que no haya autenticación previa
        if (usernameOrEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(usernameOrEmail);
                System.out.println("👤 Usuario cargado: " + userDetails.getUsername());

                // 🔹 3️⃣ Validar token JWT
                if (this.jwtUtil.validateToken(jwtToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities()
                            );
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    System.out.println("❌ Token no válido para el usuario: " + usernameOrEmail);
                }
            } catch (Exception ex) {
                System.err.println("⚠️ Error cargando usuario o validando token: " + ex.getMessage());
            }
        }

        // 🔹 4️⃣ Continuar la cadena de filtros
        filterChain.doFilter(request, response);
    }
}