package com.pooja.employee_management_system.controller;

import com.pooja.employee_management_system.entity.Employee;
import com.pooja.employee_management_system.service.EmployeeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    // Display all employees
    @GetMapping
    public String listEmployees(Model model) {

        model.addAttribute("employees", employeeService.getAllEmployees());

        return "index";
    }

    // Open Add Employee Form
    @GetMapping("/add")
    public String showAddEmployeeForm(Model model) {

        model.addAttribute("employee", new Employee());

        return "addEmployee";
    }

    // Save Employee
    @PostMapping("/save")
    public String saveEmployee(@ModelAttribute("employee") Employee employee,
                               RedirectAttributes redirectAttributes) {

        employeeService.saveEmployee(employee);

        redirectAttributes.addFlashAttribute(
                "success",
                "Employee Added Successfully!");

        return "redirect:/employees";
    }

    // Open Edit Employee Form
    @GetMapping("/edit/{id}")
    public String showEditEmployeeForm(@PathVariable Long id,
                                       Model model) {

        Employee employee = employeeService.getEmployeeById(id);

        model.addAttribute("employee", employee);

        return "editEmployee";
    }

    // Update Employee
    @PostMapping("/update/{id}")
    public String updateEmployee(@PathVariable Long id,
                                 @ModelAttribute("employee") Employee employee,
                                 RedirectAttributes redirectAttributes) {

        employeeService.updateEmployee(id, employee);

        redirectAttributes.addFlashAttribute(
                "success",
                "Employee Updated Successfully!");

        return "redirect:/employees";
    }

    // Delete Employee
    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable Long id,
                                 RedirectAttributes redirectAttributes) {

        employeeService.deleteEmployee(id);

        redirectAttributes.addFlashAttribute(
                "success",
                "Employee Deleted Successfully!");

        return "redirect:/employees";
    }


    // View Employee
    @GetMapping("/view/{id}")
    public String viewEmployee(@PathVariable Long id, Model model) {

        Employee employee = employeeService.getEmployeeById(id);

        if (employee == null) {
            return "redirect:/employees";
        }

        model.addAttribute("employee", employee);

        return "viewEmployee";
    }
}
