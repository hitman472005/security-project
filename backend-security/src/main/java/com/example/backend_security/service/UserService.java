package com.example.backend_security.service;

import com.example.backend_security.entity.Role;
import com.example.backend_security.entity.User;
import com.example.backend_security.entity.UserStatus;
import com.example.backend_security.repository.RoleRepository;
import com.example.backend_security.repository.UserRepository;
import com.example.backend_security.repository.UserStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserStatusRepository statusRepository;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, UserStatusRepository statusRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.statusRepository = statusRepository;
    }

    // =========================
    // Crear un usuario
    // =========================
    public User createUser(User user, String roleName, String statusCode) throws Exception {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new Exception("Username already exists");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new Exception("Email already exists");
        }

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new Exception("Role not found"));
        UserStatus status = statusRepository.findByCode(statusCode)
                .orElseThrow(() -> new Exception("Status not found"));

        user.setRole(role);
        user.setStatus(status);
        user.setCreationDate(LocalDateTime.now());

        return userRepository.save(user);
    }

    // =========================
    // Crear un google
    // =========================
    public User registerOrUpdateOAuthUser(Map<String, Object> userInfo) throws Exception {
        String email = (String) userInfo.get("email");
        String name = (String) userInfo.get("name");
        String photoUrl = (String) userInfo.get("picture");

        // Buscar si el usuario ya existe
        return userRepository.findByEmail(email).map(user -> {
            // Actualiza los campos si el usuario ya existe
            user.setName(name);
            user.setPhotoUrl(photoUrl);
            return userRepository.save(user);
        }).orElseGet(() -> {
            try {
                // Crea un nuevo usuario con role y status por defecto
                User newUser = new User();
                newUser.setEmail(email);
                newUser.setName(name);
                newUser.setPhotoUrl(photoUrl);
                newUser.setProvider("google");
                newUser.setCreationDate(LocalDateTime.now());


                // Asignar Role por defecto
                Role defaultRole = roleRepository.findByName("ROLE_USER")
                        .orElseThrow(() -> new Exception("Default role not found"));
                newUser.setRole(defaultRole);

                // Asignar Status por defecto
                UserStatus defaultStatus = statusRepository.findByCode("ACTIVE")
                        .orElseThrow(() -> new Exception("Default status not found"));
                newUser.setStatus(defaultStatus);

                return userRepository.save(newUser);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }


    // =========================
    // Buscar usuario por ID
    // =========================
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // =========================
    // Buscar usuario por username
    // =========================
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // =========================
    // Buscar usuario por email
    // =========================
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // =========================
    // Listar todos los usuarios
    // =========================
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // =========================
    // Actualizar usuario
    // =========================
    public User updateUser(Long id, User updatedUser) throws Exception {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new Exception("User not found"));

        existingUser.setName(updatedUser.getName());
        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPassword(updatedUser.getPassword());
        existingUser.setPhotoUrl(updatedUser.getPhotoUrl());
        existingUser.setProfilePhoto(updatedUser.getProfilePhoto());

        // Si quieres actualizar Role o Status
        if (updatedUser.getRole() != null) {
            existingUser.setRole(updatedUser.getRole());
        }
        if (updatedUser.getStatus() != null) {
            existingUser.setStatus(updatedUser.getStatus());
        }

        return userRepository.save(existingUser);
    }

    // =========================
    // Eliminar usuario
    // =========================
    public void deleteUser(Long id) throws Exception {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new Exception("User not found"));
        userRepository.delete(user);
    }
}
