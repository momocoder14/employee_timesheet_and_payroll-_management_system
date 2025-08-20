package com.employee.payroll.controller;

import com.employee.payroll.DTOs.EmployeeDTO;
import com.employee.payroll.entity.Employee;
import com.employee.payroll.service.EmployeeService;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);
    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> all() {
        List<EmployeeDTO> dtos = service.list().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> getById(@PathVariable Long id) {
        Employee employee = service.getById(id);
        return ResponseEntity.ok(toDTO(employee));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDTO> update(@PathVariable Long id, @Valid @RequestBody EmployeeDTO dto) {
        logger.info("Updating employee ID {}: {}", id, dto.getEmail());
        Employee updated = service.update(id, fromDTO(dto));
        return ResponseEntity.ok(toDTO(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logger.info("Deleting employee with ID: {}", id);
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private EmployeeDTO toDTO(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(employee.getId());
        dto.setFullName(employee.getFullName());
        dto.setEmail(employee.getEmail());
        dto.setDepartment(employee.getDepartment());
        dto.setHourlyRate(employee.getHourlyRate());
        dto.setTaxRate(employee.getTaxRate());
        return dto;
    }

    private Employee fromDTO(EmployeeDTO dto) {
        Employee employee = new Employee();
        employee.setFullName(dto.getFullName());
        employee.setEmail(dto.getEmail());
        employee.setDepartment(dto.getDepartment());
        employee.setHourlyRate(dto.getHourlyRate());
        employee.setTaxRate(dto.getTaxRate());
        return employee;
    }
}
