package com.employee.payroll.DTOs;

import java.util.List;

import com.employee.payroll.entity.Employee;

public class CurrentUserResponse {
  private String username;
  private List<String> roles;
  private Employee employee;

  public CurrentUserResponse(String username, List<String> roles, Employee employee) {
    this.username = username;
    this.roles = roles;
    this.employee = employee;
  }

  // Getters and setters (or use Lombok @Data)

  public String getUsername() {
    return username;
  }

  public List<String> getRoles() {
    return roles;
  }

  public Employee getEmployee() {
    return employee;
  }
}
