package com.example.backend_security.service;

import com.example.backend_security.entity.UserStatus;
import com.example.backend_security.repository.UserStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserStatusService {

    private final UserStatusRepository statusRepository;

    @Autowired
    public UserStatusService(UserStatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    // Crear Status
    public UserStatus createStatus(UserStatus status) throws Exception {
        if (statusRepository.findByCode(status.getCode()).isPresent()) {
            throw new Exception("Status already exists");
        }
        return statusRepository.save(status);
    }

    // Obtener todos los Status
    public List<UserStatus> getAllStatuses() {
        return statusRepository.findAll();
    }

    // Obtener Status por ID
    public Optional<UserStatus> getStatusById(Long id) {
        return statusRepository.findById(id);
    }

    // Actualizar Status
    public UserStatus updateStatus(Long id, UserStatus updatedStatus) throws Exception {
        UserStatus status = statusRepository.findById(id)
                .orElseThrow(() -> new Exception("Status not found"));

        status.setCode(updatedStatus.getCode());
        status.setDescription(updatedStatus.getDescription());

        return statusRepository.save(status);
    }

    // Eliminar Status
    public void deleteStatus(Long id) throws Exception {
        UserStatus status = statusRepository.findById(id)
                .orElseThrow(() -> new Exception("Status not found"));
        statusRepository.delete(status);
    }
}