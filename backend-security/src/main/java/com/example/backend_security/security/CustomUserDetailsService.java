package com.example.backend_security.security;

import com.example.backend_security.entity.User;
import com.example.backend_security.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository usuarioRepository;

    public CustomUserDetailsService(UserRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        // Buscar usuario por username o email
        User user = usuarioRepository.findByUsername(login)
                .orElseGet(() -> usuarioRepository.findByEmail(login)
                        .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + login)));
System.out.println("USER"+user.toString());
        // Devolver CustomUserDetails
        return new CustomUserDetails(user);
    }

}