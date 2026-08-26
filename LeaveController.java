package com.pooja.employee_management_system.controller;

import com.pooja.employee_management_system.entity.Employee;
import com.pooja.employee_management_system.entity.Leave;
import com.pooja.employee_management_system.entity.LeaveStatus;
import com.pooja.employee_management_system.entity.User;
import com.pooja.employee_management_system.repository.EmployeeRepository;
import com.pooja.employee_management_system.service.LeaveService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Controller
public class LeaveController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private LeaveService leaveService;

    // ==========================
    // Open Apply Leave Page
    // ==========================

    @GetMapping("/employee/applyLeave")
    public String applyLeavePage(HttpSession session, Model model) {

        User user = (User) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/";
        }

        Employee employee = employeeRepository
                .findByEmail(user.getEmail())
                .orElseThrow(() -> new RuntimeException("Employee Not Found"));

        model.addAttribute("employee", employee);
        model.addAttribute("leave", new Leave());

        return "applyLeave";
    }

    // ==========================
    // Submit Leave Request
    // ==========================

    @PostMapping("/employee/applyLeave")
    public String applyLeave(@ModelAttribute("leave") Leave leave,
                             HttpSession session) {

        User user = (User) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/";
        }

        Employee employee = employeeRepository
                .findByEmail(user.getEmail())
                .orElseThrow(() -> new RuntimeException("Employee Not Found"));

        // Connect leave request with logged-in employee
        leave.setEmployee(employee);

        // Set application date
        leave.setAppliedDate(LocalDate.now());

        // Every new request starts as PENDING
        leave.setStatus(LeaveStatus.PENDING);

        // Calculate total leave days
        int totalDays = (int) ChronoUnit.DAYS.between(
                leave.getFromDate(),
                leave.getToDate()
        ) + 1;

        leave.setTotalDays(totalDays);

        // Save leave request into database
        leaveService.applyLeave(leave);

        // Send success message to dashboard
        session.setAttribute(
                "leaveSuccess",
                "Leave request submitted successfully!"
        );

        return "redirect:/employee/dashboard";
    }
}
