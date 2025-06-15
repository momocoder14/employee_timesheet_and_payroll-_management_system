package com.assignment.etapms.Controller;

import com.assignment.etapms.Model.Employee;
import com.assignment.etapms.Repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepo;

    @GetMapping("/employees")
    public String list(Model model) {
        model.addAttribute("employees", employeeRepo.findAll());
        return "employee_list"; // Thymeleaf view
    }

    @GetMapping("/employee/new")
    public String createForm(Model model) {
        model.addAttribute("employee", new Employee());
        return "employee_form"; // Thymeleaf form view
    }

    @PostMapping("/employee/save")
    public String save(@ModelAttribute("employee") Employee emp) {
        employeeRepo.save(emp);
        return "redirect:/employees";
    }

    @GetMapping("/employee/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Employee emp = employeeRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid employee ID: " + id));
        model.addAttribute("employee", emp);
        return "employee_form";
    }

    @GetMapping("/employee/delete/{id}")
    public String delete(@PathVariable Long id) {
        employeeRepo.deleteById(id);
        return "redirect:/employees";
    }
}


