package com.employee.payroll.controller;

import com.employee.payroll.DTOs.TimesheetDTO;
import com.employee.payroll.entity.Timesheet;
import com.employee.payroll.service.TimesheetService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/timesheet")
public class TimesheetController {

    private final TimesheetService service;

    public TimesheetController(TimesheetService service) {
        this.service = service;
    }

    @PostMapping("/{empId}")
    public ResponseEntity<TimesheetDTO> create(@PathVariable Long empId, @Valid @RequestBody TimesheetDTO dto) {
        Timesheet timesheet = new Timesheet();
        timesheet.setDate(dto.getDate());
        timesheet.setHoursWorked(dto.getHoursWorked());

        Timesheet saved = service.create(empId, timesheet);
        return new ResponseEntity<>(toDTO(saved), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TimesheetDTO> getById(@PathVariable Long id) {
        Timesheet timesheet = service.getById(id);
        return ResponseEntity.ok(toDTO(timesheet));
    }

    @GetMapping("/employee/{empId}")
    public ResponseEntity<List<TimesheetDTO>> getByEmployee(@PathVariable Long empId) {
        List<TimesheetDTO> dtos = service.getByEmployee(empId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/employee/{empId}/range")
    public ResponseEntity<?> getByEmployeeAndDateRange(
            @PathVariable Long empId,
            @RequestParam String from,
            @RequestParam String to) {
        try {
            LocalDate fromDate = LocalDate.parse(from);
            LocalDate toDate = LocalDate.parse(to);

            if (fromDate.isAfter(toDate)) {
                return ResponseEntity.badRequest().body("Invalid date range: 'from' is after 'to'");
            }

            List<TimesheetDTO> dtos = service.getByEmployeeAndDateRange(empId, fromDate, toDate).stream()
                    .map(this::toDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(dtos);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Invalid date format. Expected yyyy-MM-dd");
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{id}/approve")
    public ResponseEntity<TimesheetDTO> approve(@PathVariable Long id) {
        Timesheet approved = service.approve(id);
        return ResponseEntity.ok(toDTO(approved));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TimesheetDTO> updateStatus(
            @PathVariable Long id,
            @RequestParam Timesheet.TimesheetStatus status) {
        Timesheet updated = service.updateStatus(id, status);
        return ResponseEntity.ok(toDTO(updated));
    }

    // DTO Conversion Helpers
    private TimesheetDTO toDTO(Timesheet ts) {
        TimesheetDTO dto = new TimesheetDTO();
        dto.setId(ts.getId());
        dto.setDate(ts.getDate());
        dto.setHoursWorked(ts.getHoursWorked());
        dto.setStatus(ts.getStatus());
        dto.setEmployeeId(ts.getEmployee().getId());
        return dto;
    }

}
