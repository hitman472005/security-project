package com.example.backend_security.service;

import com.example.backend_security.dto.RegisterRequest;
import com.example.backend_security.entity.Role;
import com.example.backend_security.entity.User;
import com.example.backend_security.entity.UserStatus;
import com.example.backend_security.exception.ResourceNotFoundException;
import com.example.backend_security.exception.UserAlreadyExistsException;
import com.example.backend_security.repository.RoleRepository;
import com.example.backend_security.repository.UserRepository;
import com.example.backend_security.repository.UserStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, UserStatusRepository statusRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.statusRepository = statusRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // =========================
    // Crear un usuario
    // =========================
    public User createUser(RegisterRequest request) {
        // Validar si el username ya existe
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists: " + request.getUsername());
        }

        // Validar si el email ya existe
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists: " + request.getEmail());
        }

        // Obtener rol por defecto
        Role defaultRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new ResourceNotFoundException("Default role not found"));

        // Obtener estado por defecto
        UserStatus defaultStatus = statusRepository.findByCode("ACTIVE")
                .orElseThrow(() -> new ResourceNotFoundException("Default status not found"));

        // Crear el usuario
        User newUser = new User();
        newUser.setName(request.getName());
        newUser.setUsername(request.getUsername());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setRole(defaultRole);
        newUser.setStatus(defaultStatus);
        newUser.setCreationDate(LocalDateTime.now());

        return userRepository.save(newUser);
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
    public User updateUser(Long userId, RegisterRequest updatedUser) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID " + userId + " not found"));

        if (!user.getUsername().equals(updatedUser.getUsername()) && userRepository.existsByUsername(updatedUser.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        if (!user.getEmail().equals(updatedUser.getEmail()) && userRepository.existsByEmail(updatedUser.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Actualizar campos
        user.setName(updatedUser.getName());
        user.setUsername(updatedUser.getUsername());
        user.setEmail(updatedUser.getEmail());
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        return userRepository.save(user);
    }


}
