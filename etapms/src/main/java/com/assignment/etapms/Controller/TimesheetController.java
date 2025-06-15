package com.assignment.etapms.Controller;

import com.assignment.etapms.Model.Timesheet;
import com.assignment.etapms.Repository.EmployeeRepository;
import com.assignment.etapms.Repository.TimesheetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class TimesheetController {

    @Autowired
    private TimesheetRepository timesheetRepo;

    @Autowired
    private EmployeeRepository employeeRepo;

    @GetMapping("/timesheet/new")
    public String newEntry(Model model) {
        model.addAttribute("entry", new Timesheet());
        model.addAttribute("employees", employeeRepo.findAll());
        return "timesheet_form"; // Thymeleaf form view
    }

    @PostMapping("/timesheet/save")
    public String save(@ModelAttribute("entry") Timesheet entry) {
        timesheetRepo.save(entry);
        return "redirect:/employees"; // or redirect to a timesheet list view
    }
}

