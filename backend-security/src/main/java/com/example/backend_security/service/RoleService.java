package com.example.backend_security.service;

import com.example.backend_security.entity.Role;
import com.example.backend_security.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    // Crear Role
    public Role createRole(Role role) throws Exception {
        if (roleRepository.findByName(role.getName()).isPresent()) {
            throw new Exception("Role already exists");
        }
        return roleRepository.save(role);
    }

    // Obtener todos los Roles
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    // Obtener Role por ID
    public Optional<Role> getRoleById(Long id) {
        return roleRepository.findById(id);
    }

    // Actualizar Role
    public Role updateRole(Long id, Role updatedRole) throws Exception {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new Exception("Role not found"));

        role.setName(updatedRole.getName());
        role.setDescription(updatedRole.getDescription());

        return roleRepository.save(role);
    }

    // Eliminar Role
    public void deleteRole(Long id) throws Exception {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new Exception("Role not found"));
        roleRepository.delete(role);
    }
}