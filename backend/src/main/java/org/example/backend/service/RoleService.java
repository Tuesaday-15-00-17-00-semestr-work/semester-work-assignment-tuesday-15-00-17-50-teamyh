package org.example.backend.service;

import org.example.backend.model.Role;
import org.example.backend.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    // Метод для получения всех ролей
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    // Метод для сохранения роли
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }
}
