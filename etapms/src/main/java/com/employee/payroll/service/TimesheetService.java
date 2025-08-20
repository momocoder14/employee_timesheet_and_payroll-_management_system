package com.employee.payroll.service;

import com.employee.payroll.entity.Employee;
import com.employee.payroll.entity.Timesheet;
import com.employee.payroll.repository.EmployeeRepository;
import com.employee.payroll.repository.TimesheetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
@Service
public class TimesheetService {
    @Autowired
    private TimesheetRepository timesheetRepo;
    @Autowired
    private EmployeeRepository employeeRepo;

    public Timesheet create(Long empId, Timesheet ts) {
        Employee emp = employeeRepo.findById(empId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with ID: " + empId));

        if (ts.getDate().isAfter(LocalDate.now())) {
            throw new InvalidTimesheetException("Cannot log timesheet for future date: " + ts.getDate());
        }

        ts.setEmployee(emp);
        ts.setStatus(Timesheet.TimesheetStatus.SUBMITTED);
        return timesheetRepo.save(ts);
    }

    public Timesheet getById(Long id) {
        return timesheetRepo.findById(id)
                .orElseThrow(() -> new TimesheetNotFoundException("Timesheet not found with ID: " + id));
    }

    public Timesheet approve(Long id) {
        Timesheet timesheet = getById(id);
        timesheet.setStatus(Timesheet.TimesheetStatus.valueOf("APPROVED"));
        return timesheetRepo.save(timesheet);
    }

    public List<Timesheet> getByEmployee(Long empId) {
        return timesheetRepo.findByEmployeeId(empId);
    }

    public List<Timesheet> getByEmployeeAndDateRange(Long empId, LocalDate from, LocalDate to) {
        return timesheetRepo.findByEmployeeIdAndDateBetween(empId, from, to);
    }

    public Timesheet updateStatus(Long timesheetId, Timesheet.TimesheetStatus status) {
        Timesheet ts = timesheetRepo.findById(timesheetId)
                .orElseThrow(() -> new TimesheetNotFoundException("Timesheet not found with ID: " + timesheetId));
        ts.setStatus(status);
        return timesheetRepo.save(ts);
    }
}

class TimesheetNotFoundException extends RuntimeException {
    public TimesheetNotFoundException(String message) {
        super(message);
    }
}
class InvalidTimesheetException extends RuntimeException {
    public InvalidTimesheetException(String message) {
        super(message);
    }
}