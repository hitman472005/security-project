package com.example.backend_security.service;

import com.example.backend_security.entity.Permission;
import com.example.backend_security.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class PermissionService {

    private final PermissionRepository permissionRepository;

    @Autowired
    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    // Crear Permission
    public Permission createPermission(Permission permission) throws Exception {
        if (permissionRepository.findByCode(permission.getCode()).isPresent()) {
            throw new Exception("Permission already exists");
        }
        return permissionRepository.save(permission);
    }

    // Obtener todos los Permissions
    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }

    // Obtener Permission por ID
    public Optional<Permission> getPermissionById(Long id) {
        return permissionRepository.findById(id);
    }

    // Actualizar Permission
    public Permission updatePermission(Long id, Permission updatedPermission) throws Exception {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new Exception("Permission not found"));

        permission.setCode(updatedPermission.getCode());
        permission.setDescription(updatedPermission.getDescription());

        return permissionRepository.save(permission);
    }

    // Eliminar Permission
    public void deletePermission(Long id) throws Exception {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new Exception("Permission not found"));
        permissionRepository.delete(permission);
    }
}