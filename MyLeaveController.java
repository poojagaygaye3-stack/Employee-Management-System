package com.pooja.employee_management_system.controller;

import com.pooja.employee_management_system.entity.Employee;
import com.pooja.employee_management_system.entity.User;
import com.pooja.employee_management_system.repository.EmployeeRepository;
import com.pooja.employee_management_system.service.LeaveService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyLeaveController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private LeaveService leaveService;

    @GetMapping("/employee/myLeaves")
    public String myLeaves(HttpSession session, Model model) {

        User user = (User) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/";
        }

        Employee employee = employeeRepository
                .findByEmail(user.getEmail())
                .orElseThrow(() -> new RuntimeException("Employee Not Found"));

        model.addAttribute("employee", employee);

        model.addAttribute(
                "leaveList",
                leaveService.getEmployeeLeaves(employee)
        );

        return "myLeaves";
    }
}
