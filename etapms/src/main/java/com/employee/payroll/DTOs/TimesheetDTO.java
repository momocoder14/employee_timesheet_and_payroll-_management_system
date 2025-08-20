package com.employee.payroll.DTOs;

import com.employee.payroll.entity.Timesheet;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

public class TimesheetDTO {
    private Long id;
    @NotNull
    @PastOrPresent
    private LocalDate date;
    @NotNull
    @Positive
    @DecimalMax("16.0")
    private Double hoursWorked;
    private Timesheet.TimesheetStatus status;
    private Long employeeId;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public Double getHoursWorked() { return hoursWorked; }
    public void setHoursWorked(Double hoursWorked) { this.hoursWorked = hoursWorked; }
    public Timesheet.TimesheetStatus getStatus() { return status; }
    public void setStatus(Timesheet.TimesheetStatus status) { this.status = status; }
    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }
}