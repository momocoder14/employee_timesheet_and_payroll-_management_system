package com.employee.payroll.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.DecimalMax;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Size(max = 100)
    private String fullName;

    @Email
    @NotNull
    private String email;

    @NotNull
    @Size(max = 50)
    private String department;
    @NotNull
    private Double hourlyRate;
    @NotNull
    @DecimalMin("0.0")
    @DecimalMax("100.0")
    private Double taxRate;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany(mappedBy = "employee", cascade = CascadeType.REMOVE)
    private List<Timesheet> timesheet;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.REMOVE)
    private List<PayrollRecord> payrollRecords;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Getters and setters (unchanged)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(Double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public Double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(Double taxRate) {
        this.taxRate = taxRate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Timesheet> getTimesheet() {
        return timesheet;
    }

    public void setTimesheet(List<Timesheet> timesheet) {
        this.timesheet = timesheet;
    }

    public List<PayrollRecord> getPayrollRecords() {
        return payrollRecords;
    }

    public void setPayrollRecords(List<PayrollRecord> payrollRecords) {
        this.payrollRecords = payrollRecords;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}