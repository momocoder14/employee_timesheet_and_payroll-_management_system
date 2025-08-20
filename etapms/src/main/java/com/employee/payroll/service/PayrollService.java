package com.employee.payroll.service;

import com.employee.payroll.entity.Employee;
import com.employee.payroll.entity.PayrollRecord;
import com.employee.payroll.entity.Timesheet;
import com.employee.payroll.repository.EmployeeRepository;
import com.employee.payroll.repository.PayrollRepository;
import com.employee.payroll.repository.TimesheetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class PayrollService {
    private static final Logger logger = LoggerFactory.getLogger(PayrollService.class);
    @Autowired
    private PayrollRepository payrollRepo;
    @Autowired
    private TimesheetRepository timesheetRepo;

    @Autowired
    private EmployeeRepository employeeRepo;

    @Value("${payroll.default-tax-rate:0.1}")
    private double defaultTaxRate;

    @Value("${payroll.default-deductions:50.0}") // Example: Fixed deduction for benefits
    private double defaultDeductions;

    @Transactional
    public PayrollRecord generatePayroll(Long empId, LocalDate from, LocalDate to) {
        Employee emp = employeeRepo.findById(empId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with ID: " + empId));

        List<Timesheet> timesheets = timesheetRepo.findByEmployeeIdAndDateBetweenAndStatusIn(
                empId, from, to,
                List.of(Timesheet.TimesheetStatus.SUBMITTED.name(), Timesheet.TimesheetStatus.APPROVED.name()));

        if (timesheets.isEmpty()) {
            throw new NoApprovedTimesheetException(
                    "No submitted or approved timesheet found for employee ID: " + empId);
        }

        double hours = timesheets.stream()
                .mapToDouble(Timesheet::getHoursWorked)
                .sum();

        if (hours == 0) {
            throw new NoApprovedTimesheetException("No approved timesheet found for employee ID: " + empId);
        }

        double gross = hours * emp.getHourlyRate();
        double taxRate = emp.getTaxRate() != null ? emp.getTaxRate() : defaultTaxRate;
        double tax = gross * taxRate / 100;

        double deductions = defaultDeductions; // Fixed for now, can be dynamic later

        // Adjust deductions if too high, so net pay is not negative
        if (deductions > gross - tax) {
            logger.warn("Deductions ({}) exceed gross minus tax ({} - {}). Adjusting deductions for employee ID: {}",
                    deductions, gross, tax, empId);
            deductions = Math.max(0, gross - tax);
        }

        double net = gross - tax - deductions;

        // Clamp net to zero if negative due to rounding or other issues
        if (net < 0) {
            logger.warn("Net pay is negative ({}). Setting to 0 for employee ID: {}", net, empId);
            net = 0.0;
        }

        logger.info("Payroll calculation: empId={}, hours={}, gross={}, taxRate={}, tax={}, deductions={}, net={}",
                empId, hours, gross, taxRate, tax, deductions, net);

        PayrollRecord pr = new PayrollRecord();
        pr.setEmployee(emp);
        pr.setGrossPay(gross);
        pr.setTax(tax);
        pr.setDeductions(deductions);
        pr.setNetPay(net);
        pr.setPayDate(LocalDate.now());
        pr.setPayPeriod(from.toString() + " to " + to.toString());

        return payrollRepo.save(pr);
    }

    public List<PayrollRecord> getByEmployee(Long empId) {
        return payrollRepo.findByEmployeeId(empId);
    }

    @Scheduled(cron = "0 0 0 1,15 * ?")
    @Transactional
    public void generatePayrollForAllEmployees() {
        LocalDate today = LocalDate.now();
        LocalDate from = today.minusDays(14);
        LocalDate to = today;

        List<Employee> employees = employeeRepo.findAll();
        for (Employee emp : employees) {
            try {
                generatePayroll(emp.getId(), from, to);
                logger.info("Payroll generated for employee ID: {}", emp.getId());
            } catch (Exception e) {
                logger.error("Failed to generate payroll for employee ID: {}", emp.getId(), e);
            }
        }
    }

    public Double getTotalGrossPay(Long empId, LocalDate from, LocalDate to) {
        return payrollRepo.findByEmployeeIdAndPayDateBetween(empId, from, to)
                .stream()
                .mapToDouble(PayrollRecord::getGrossPay)
                .sum();
    }
}

class NoApprovedTimesheetException extends RuntimeException {
    public NoApprovedTimesheetException(String message) {
        super(message);
    }
}