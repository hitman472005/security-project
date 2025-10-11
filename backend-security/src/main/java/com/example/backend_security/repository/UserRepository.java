package com.example.backend_security.repository;


import com.example.backend_security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    List<User> findByRole_Name(String roleName);
    List<User> findByRole_NameAndStatus_Code(String roleName, String statusCode);
}
