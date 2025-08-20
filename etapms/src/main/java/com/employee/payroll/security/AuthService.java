package com.employee.payroll.security;

import com.employee.payroll.repository.EmployeeRepository;
import com.employee.payroll.repository.TimesheetRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private TimesheetRepository timesheetRepo;

    @Autowired
    private EmployeeRepository employeeRepo;

    public boolean canAccessTimesheetByTimesheetId(Authentication auth, Long timesheetId) {
        return timesheetRepo.findById(timesheetId)
                .map(ts -> {
                    Long employeeId = ts.getEmployee().getId();
                    return auth.getAuthorities().stream()
                            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))
                            || employeeId.equals(getEmployeeIdByEmail(auth.getName()));
                }).orElse(false);
    }

    private Long getEmployeeIdByEmail(String email) {
        return employeeRepo.findByEmail(email)
                .map(emp -> emp.getId())
                .orElse(null);
    }

}