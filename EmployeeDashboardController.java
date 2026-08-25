package com.pooja.employee_management_system.controller;

import com.pooja.employee_management_system.entity.Employee;
import com.pooja.employee_management_system.entity.User;
import com.pooja.employee_management_system.repository.EmployeeRepository;
import com.pooja.employee_management_system.service.AttendanceService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.time.LocalTime;

@Controller
public class EmployeeDashboardController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AttendanceService attendanceService;

    @GetMapping("/employee/dashboard")
    public String dashboard(HttpSession session, Model model) {

        // Get Logged-in User
        User user = (User) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/";
        }

        // Get Employee Details
        Employee employee = employeeRepository
                .findByEmail(user.getEmail())
                .orElseThrow(() -> new RuntimeException("Employee Not Found"));

        // Employee Information
        model.addAttribute("employee", employee);

        // Current Date & Time
        model.addAttribute("today", LocalDate.now());
        model.addAttribute("currentTime", LocalTime.now());

        // Attendance Summary
        model.addAttribute("presentCount",
                attendanceService.getPresentCount(employee));

        model.addAttribute("lateCount",
                attendanceService.getLateCount(employee));

        model.addAttribute("absentCount",
                attendanceService.getAbsentCount(employee));
        String leaveSuccess = (String) session.getAttribute("leaveSuccess");

        if (leaveSuccess != null) {
            model.addAttribute("leaveSuccess", leaveSuccess);
            session.removeAttribute("leaveSuccess");
        }

        return "employeeDashboard";
    }
}
