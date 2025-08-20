package com.employee.payroll.DTOs;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDate;

public class PayrollRequestDTO {
    @NotNull
    private Long employeeId;
    @NotNull
    @PastOrPresent
    private LocalDate from;
    @NotNull
    @PastOrPresent
    private LocalDate to;
    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }
    public LocalDate getFrom() { return from; }
    public void setFrom(LocalDate from) { this.from = from; }
    public LocalDate getTo() { return to; }
    public void setTo(LocalDate to) { this.to = to; }
}