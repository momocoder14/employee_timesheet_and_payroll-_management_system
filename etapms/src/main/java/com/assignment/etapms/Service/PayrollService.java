package com.assignment.etapms.Service;

import com.assignment.etapms.Model.Timesheet;
import com.assignment.etapms.Repository.TimesheetRepository;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PayrollService {

    private final TimesheetRepository timesheetRepo;

    //@Autowired
    public PayrollService(TimesheetRepository timesheetRepo) {
        this.timesheetRepo = timesheetRepo;
    }

    public double calculatePayroll(Long empId) {
        List<Timesheet> entries = timesheetRepo.findByEmployeeId(empId);
        return entries.stream()
                .mapToDouble(t -> t.getHoursWorked() * t.getEmployee().getHourlyRate())
                .sum();
    }
}
