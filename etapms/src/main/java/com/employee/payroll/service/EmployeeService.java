package com.employee.payroll.service;

import com.employee.payroll.entity.Employee;
import com.employee.payroll.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepo;

    public Employee create(Employee emp) {
        if (employeeRepo.findByEmail(emp.getEmail()).isPresent()) {
            throw new EmployeeAlreadyExistsException("Email already exists: " + emp.getEmail());
        }
        return employeeRepo.save(emp);
    }

    public void delete(Long id) {
        if (!employeeRepo.existsById(id)) {
            throw new EmployeeNotFoundException("Employee not found with ID: " + id);
        }
        employeeRepo.deleteById(id);
    }

    public List<Employee> list() {
        return employeeRepo.findAll();
    }

    public Employee getById(Long id) {
        return employeeRepo.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with ID: " + id));
    }

    public Employee update(Long id, Employee updatedEmp) {
        Employee emp = getById(id);
        if (!emp.getEmail().equals(updatedEmp.getEmail()) &&
                employeeRepo.findByEmail(updatedEmp.getEmail()).isPresent()) {
            throw new EmployeeAlreadyExistsException("Email already exists: " + updatedEmp.getEmail());
        }
        emp.setFullName(updatedEmp.getFullName());
        emp.setEmail(updatedEmp.getEmail());
        emp.setDepartment(updatedEmp.getDepartment());
        emp.setHourlyRate(updatedEmp.getHourlyRate());
        emp.setTaxRate(updatedEmp.getTaxRate());
        return employeeRepo.save(emp);
    }

    public List<Employee> findByDepartment(String department) {
        return employeeRepo.findByDepartment(department);
    }

    public Optional<Employee> findByEmail(String email) {
        return employeeRepo.findByEmail(email);
    }
}

class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException(String message) {
        super(message);
    }
}

class EmployeeAlreadyExistsException extends RuntimeException {
    public EmployeeAlreadyExistsException(String message) {
        super(message);
    }
}