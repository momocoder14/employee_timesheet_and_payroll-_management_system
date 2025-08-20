package com.employee.payroll.service;

import com.employee.payroll.entity.Employee;
import com.employee.payroll.entity.Role;
import com.employee.payroll.entity.User;
import com.employee.payroll.repository.EmployeeRepository;
import com.employee.payroll.repository.RoleRepository;
import com.employee.payroll.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private EmployeeRepository employeeRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public User registerUser(String username, String rawPassword, List<String> roleNames,
            String fullName, String email, String department,
            Double hourlyRate, Double taxRate) {
        if (userRepo.existsByUsername(username)) {
            throw new UsernameAlreadyExistsException("Username already exists: " + username);
        }

        // Create user
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));

        Set<Role> roles = new HashSet<>();
        for (String roleName : roleNames) {
            Role role = roleRepo.findByName(roleName)
                    .orElseThrow(() -> new RoleNotFoundException("Role not found: " + roleName));
            roles.add(role);
        }
        user.setRoles(roles);

        // Create employee
        Employee employee = new Employee();
        employee.setFullName(fullName);
        employee.setEmail(email);
        employee.setDepartment(department);
        employee.setHourlyRate(hourlyRate);
        employee.setTaxRate(taxRate);
        employee.setUser(user);

        user.setEmployee(employee); // Link user ↔ employee

        employeeRepo.save(employee); // Persist employee first if needed

        return userRepo.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    public User save(User user) {
        return userRepo.save(user);
    }

    // find employee by username
    public Optional<Employee> findEmployeeByUsername(String username) {
        return userRepo.findByUsername(username)
                .map(User::getEmployee);
    }
}

class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException(String message) {
        super(message);
    }
}

class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(String message) {
        super(message);
    }
}