package com.pooja.employee_management_system.controller;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pooja.employee_management_system.entity.Attendance;
import com.pooja.employee_management_system.entity.Employee;
import com.pooja.employee_management_system.entity.User;
import com.pooja.employee_management_system.repository.EmployeeRepository;
import com.pooja.employee_management_system.service.AttendanceService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private EmployeeRepository employeeRepository;

    // Employee Attendance Page
    @GetMapping("/employee/attendance")
    public String attendancePage(HttpSession session, Model model) {

        User user = (User) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/";
        }

        Employee employee = employeeRepository
                .findByEmail(user.getEmail())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        Attendance todayAttendance =
                attendanceService.getTodayAttendance(employee);

        model.addAttribute("todayAttendance", todayAttendance);

        model.addAttribute("presentCount",
                attendanceService.getPresentCount(employee));

        model.addAttribute("lateCount",
                attendanceService.getLateCount(employee));

        model.addAttribute("absentCount",
                attendanceService.getAbsentCount(employee));

        return "attendance";
    }

    @PostMapping("/employee/attendance/mark")
    public String markAttendance(HttpSession session,
                                 RedirectAttributes redirectAttributes) {

        User user = (User) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/";
        }

        Employee employee = employeeRepository
                .findByEmail(user.getEmail())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        try {
            attendanceService.markAttendance(employee);
            redirectAttributes.addFlashAttribute("success",
                    "Attendance marked successfully.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error",
                    e.getMessage());
        }

        return "redirect:/employee/attendance";
    }



    @PostMapping("/employee/attendance/checkout")
    public String checkOut(HttpSession session,
                           RedirectAttributes redirectAttributes) {

        User user = (User) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/";
        }

        Employee employee = employeeRepository
                .findByEmail(user.getEmail())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        try {

            attendanceService.checkOut(employee);

            redirectAttributes.addFlashAttribute(
                    "success",
                    "Checked out successfully."
            );

        } catch (RuntimeException e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage()
            );

        }

        return "redirect:/employee/attendance";
    }

    // Attendance History
    @GetMapping("/employee/attendance/history")
    public String attendanceHistory(HttpSession session, Model model) {

        User user = (User) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/";
        }

        Employee employee = employeeRepository
                .findByEmail(user.getEmail())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        model.addAttribute("attendanceHistory",
                attendanceService.getAttendanceHistory(employee));

        return "attendanceHistory";
    }
}
