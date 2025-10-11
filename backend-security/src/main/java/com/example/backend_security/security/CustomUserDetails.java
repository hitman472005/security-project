package com.example.backend_security.security;

import com.example.backend_security.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    // ✅ Roles / Authorities
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (user.getRole() != null) {
            return Collections.singletonList(
                    new SimpleGrantedAuthority(user.getRole().getName())
            );
        }
        return Collections.emptyList();
    }

    // ✅ Password (puede ser null si viene de Google)
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    // ✅ Devuelve username o email según corresponda
    @Override
    public String getUsername() {
        // Si el usuario fue creado con Google no tendrá username
        if (user.getUsername() != null && !user.getUsername().isEmpty()) {
            return user.getUsername();
        }
        // Usa email como identificador alternativo
        return user.getEmail();
    }

    // ✅ Cuenta nunca expira (puedes ajustar si quieres)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // ✅ Bloqueo basado en el status del usuario
    @Override
    public boolean isAccountNonLocked() {
        if (user.getStatus() != null) {
            return !"BLOCKED".equalsIgnoreCase(user.getStatus().getCode());
        }
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // ✅ Activo solo si no está bloqueado
    @Override
    public boolean isEnabled() {
        if (user.getStatus() != null) {
            return !"BLOCKED".equalsIgnoreCase(user.getStatus().getCode());
        }
        return true;
    }

    // ✅ Getter para acceder al usuario real si se necesita
    public User getUser() {
        return user;
    }

    // ✅ Override de toString (útil para depuración)
    @Override
    public String toString() {
        return "CustomUserDetails{" +
                "username='" + getUsername() + '\'' +
                ", role=" + (user.getRole() != null ? user.getRole().getName() : "sin rol") +
                ", status=" + (user.getStatus() != null ? user.getStatus().getCode() : "N/A") +
                '}';
    }
}