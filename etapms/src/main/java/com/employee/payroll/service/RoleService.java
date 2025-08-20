package com.employee.payroll.service;

import com.employee.payroll.entity.Role;
import com.employee.payroll.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.Optional;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepo;

    public Role createRole(String name) {
        if (roleRepo.existsByName(name)) {
            throw new RuntimeException("Role already exists: " + name);
        }
        Role role = new Role();
        role.setName(name);
        return roleRepo.save(role);
    }

    public Optional<Role> findByName(String name) {
        return roleRepo.findByName(name);
    }

    @PostConstruct
    public void initRoles() {
        if (!roleRepo.existsByName("ROLE_ADMIN")) {
            createRole("ROLE_ADMIN");
        }
        if (!roleRepo.existsByName("ROLE_EMPLOYEE")) {
            createRole("ROLE_EMPLOYEE");
        }

    }

}