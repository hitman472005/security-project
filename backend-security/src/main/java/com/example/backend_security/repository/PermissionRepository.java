package com.example.backend_security.repository;

import com.example.backend_security.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionsRepository extends JpaRepository<Permission,Long> {
}
