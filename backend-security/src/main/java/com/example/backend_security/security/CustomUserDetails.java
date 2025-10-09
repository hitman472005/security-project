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

    // Roles / Authorities
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (user.getRole() != null) {
            return Collections.singletonList(
                    new SimpleGrantedAuthority(user.getRole().getName())
            );
        }
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    // La cuenta nunca expira en este ejemplo
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // Bloqueo basado en el status del usuario
    @Override
    public boolean isAccountNonLocked() {
        if (user.getStatus() != null) {
            // Por ejemplo, si el status es "BLOCKED", se bloquea la cuenta
            return !"BLOCKED".equalsIgnoreCase(user.getStatus().getCode());
        }
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // Activo solo si el status no es bloqueado
    @Override
    public boolean isEnabled() {
        if (user.getStatus() != null) {
            return !"BLOCKED".equalsIgnoreCase(user.getStatus().getCode());
        }
        return true;
    }

    // Getter para acceder al usuario real si se necesita
    public User getUser() {
        return user;
    }
}