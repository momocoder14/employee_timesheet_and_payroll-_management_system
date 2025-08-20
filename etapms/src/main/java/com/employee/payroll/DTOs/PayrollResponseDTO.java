package com.employee.payroll.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDate;

public class PayrollResponseDTO {
    private Long id;
    private LocalDate payDate;
    @PositiveOrZero
    private Double grossPay;
    @PositiveOrZero
    private Double tax;
    @PositiveOrZero
    private Double netPay;
    @PositiveOrZero
    private Double deductions;
    @NotBlank
    private String payPeriod;
    private Long employeeId;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDate getPayDate() { return payDate; }
    public void setPayDate(LocalDate payDate) { this.payDate = payDate; }
    public Double getGrossPay() { return grossPay; }
    public void setGrossPay(Double grossPay) { this.grossPay = grossPay; }
    public Double getTax() { return tax; }
    public void setTax(Double tax) { this.tax = tax; }
    public Double getNetPay() { return netPay; }
    public void setNetPay(Double netPay) { this.netPay = netPay; }
    public Double getDeductions() { return deductions; }
    public void setDeductions(Double deductions) { this.deductions = deductions; }
    public String getPayPeriod() { return payPeriod; }
    public void setPayPeriod(String payPeriod) { this.payPeriod = payPeriod; }
    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }
}