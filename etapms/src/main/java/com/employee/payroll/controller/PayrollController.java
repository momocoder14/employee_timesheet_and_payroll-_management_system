package com.employee.payroll.controller;

import com.employee.payroll.DTOs.PayrollRequestDTO;
import com.employee.payroll.DTOs.PayrollResponseDTO;
import com.employee.payroll.entity.PayrollRecord;
import com.employee.payroll.service.PayrollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/payroll")
public class PayrollController {
    @Autowired
    private PayrollService service;

    @PostMapping("/generate")
    public ResponseEntity<PayrollResponseDTO> generate(@Valid @RequestBody PayrollRequestDTO request) {
        if (request.getFrom().isAfter(request.getTo())) {
            return ResponseEntity.badRequest().body(null);
        }
        PayrollRecord record = service.generatePayroll(
                request.getEmployeeId(), request.getFrom(), request.getTo());
        return new ResponseEntity<>(toDTO(record), HttpStatus.CREATED);
    }

    @GetMapping("/employee/{empId}")
    public ResponseEntity<List<PayrollResponseDTO>> getByEmployee(@PathVariable Long empId) {
        List<PayrollResponseDTO> dtos = service.getByEmployee(empId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    private PayrollResponseDTO toDTO(PayrollRecord record) {
        PayrollResponseDTO dto = new PayrollResponseDTO();
        dto.setId(record.getId());
        dto.setPayDate(record.getPayDate());
        dto.setGrossPay(record.getGrossPay());
        dto.setTax(record.getTax());
        dto.setNetPay(record.getNetPay());
        dto.setDeductions(record.getDeductions());
        dto.setPayPeriod(record.getPayPeriod());
        dto.setEmployeeId(record.getEmployee().getId());
        return dto;
    }
}