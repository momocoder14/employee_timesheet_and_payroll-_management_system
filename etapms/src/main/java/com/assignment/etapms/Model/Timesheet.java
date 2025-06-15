package com.assignment.etapms.Model;

import jakarta.persistence.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class Timesheet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private LocalTime clockIn;
    private LocalTime clockOut;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    public Timesheet() {
    }

    public Timesheet(LocalDate date, LocalTime clockIn, LocalTime clockOut, Employee employee) {
        this.date = date;
        this.clockIn = clockIn;
        this.clockOut = clockOut;
        this.employee = employee;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getClockIn() {
        return clockIn;
    }

    public void setClockIn(LocalTime clockIn) {
        this.clockIn = clockIn;
    }

    public LocalTime getClockOut() {
        return clockOut;
    }

    public void setClockOut(LocalTime clockOut) {
        this.clockOut = clockOut;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public double getHoursWorked() {
        return Duration.between(clockIn, clockOut).toHours();
    }
}

