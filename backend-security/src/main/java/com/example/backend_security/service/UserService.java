package com.example.backend_security.service;

import com.example.backend_security.dto.LoginRequest;
import com.example.backend_security.dto.RegisterRequest;
import com.example.backend_security.dto.TokenResponse;
import com.example.backend_security.entity.Role;
import com.example.backend_security.entity.User;
import com.example.backend_security.entity.UserStatus;
import com.example.backend_security.exception.ResourceNotFoundException;
import com.example.backend_security.exception.UserAlreadyExistsException;
import com.example.backend_security.repository.RoleRepository;
import com.example.backend_security.repository.UserRepository;
import com.example.backend_security.repository.UserStatusRepository;
import com.example.backend_security.security.CustomUserDetailsService;
import com.example.backend_security.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
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
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtils;
    private final TokenService tokenService;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, UserStatusRepository statusRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtil jwtUtils, TokenService tokenService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.statusRepository = statusRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.tokenService = tokenService;

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


    // =========================
    // 🔹 LOGIN (Generar token)
    // =========================
    public TokenResponse login(LoginRequest loginRequest) {
        String identificador = loginRequest.getLogin();
        String password = loginRequest.getPassword();

        try {
            // Autenticación con Spring Security
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(identificador, password)
            );

            // Buscar usuario por username o email
            User user = userRepository.findByUsername(identificador)
                    .orElseGet(() -> userRepository.findByEmail(identificador)
                            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + identificador)));

            // 🔹 Generar token
            String token = jwtUtils.generateToken(user);
            tokenService.createToken(user.getId(), token);
            System.out.println("🟢 Token generado: " + token);

            return new TokenResponse(token);

        } catch (BadCredentialsException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Usuario o contraseña incorrectos", ex);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Error al iniciar sesión", ex);
        }
    }

    public void validarIdentificador(String identificador) {
        boolean existe;
        if (esCorreo(identificador)) {
            existe = userRepository.existsByEmail(identificador);
        } else {
            existe = userRepository.existsByUsername(identificador);
        }
        if (!existe) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
    }

    private boolean esCorreo(String valor) {
        return valor != null && valor.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    public User actualUsuario(Principal principal) {
        if (principal == null || principal.getName() == null) {
            throw new RuntimeException("Usuario no autorizado");
        }
        User user = userRepository.findByUsername(principal.getName())
                .orElseGet(() -> userRepository.findByEmail(principal.getName())
                        .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + principal.getName())));

        return user;
    }

    public List<User> getUsersByRoleUser() {
        return userRepository.findByRole_Name("ROLE_USER");
    }

    public List<User> getUsersByRoleAdmin() {
        return userRepository.findByRole_Name("ROLE_ADMIN");
    }

    // ------------------ ROLE_USER ------------------
    public List<User> getActiveUsersByRoleUser() {
        return userRepository.findByRole_NameAndStatus_Code("ROLE_USER", "ACTIVE");
    }

    public List<User> getSuspendedUsersByRoleUser() {
        return userRepository.findByRole_NameAndStatus_Code("ROLE_USER", "SUSPEND");
    }

    public List<User> getInactiveUsersByRoleUser() {
        return userRepository.findByRole_NameAndStatus_Code("ROLE_USER", "INACTIVE");
    }

    public List<User> getBlockedUsersByRoleUser() {
        return userRepository.findByRole_NameAndStatus_Code("ROLE_USER", "BLOCKED");
    }

    // ------------------ ROLE_ADMIN ------------------
    public List<User> getActiveUsersByRoleAdmin() {
        return userRepository.findByRole_NameAndStatus_Code("ROLE_ADMIN", "ACTIVE");
    }

    public List<User> getSuspendedUsersByRoleAdmin() {
        return userRepository.findByRole_NameAndStatus_Code("ROLE_ADMIN", "SUSPEND");
    }

    public List<User> getInactiveUsersByRoleAdmin() {
        return userRepository.findByRole_NameAndStatus_Code("ROLE_ADMIN", "INACTIVE");
    }

    public List<User> getBlockedUsersByRoleAdmin() {
        return userRepository.findByRole_NameAndStatus_Code("ROLE_ADMIN", "BLOCKED");
    }
}
