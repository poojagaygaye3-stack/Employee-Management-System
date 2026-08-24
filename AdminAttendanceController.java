package com.pooja.employee_management_system.controller;

import com.pooja.employee_management_system.entity.Attendance;
import com.pooja.employee_management_system.entity.AttendanceStatus;
import com.pooja.employee_management_system.repository.AttendanceRepository;
import com.pooja.employee_management_system.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
public class AdminAttendanceController {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    // Admin Attendance Page
    @GetMapping("/admin/attendance")
    public String adminAttendance(
            @RequestParam(required = false) String filter,
            Model model) {

        LocalDate today = LocalDate.now();

        List<Attendance> attendanceList;

        // Filter logic
        if ("present".equalsIgnoreCase(filter)) {
            attendanceList = attendanceRepository.findByDateAndStatus(
                    today,
                    AttendanceStatus.PRESENT
            );
        } else if ("late".equalsIgnoreCase(filter)) {
            attendanceList = attendanceRepository.findByDateAndStatus(
                    today,
                    AttendanceStatus.LATE
            );
        } else {
            // Default: today's attendance
            attendanceList = attendanceRepository.findByDate(today);
        }

        // Dashboard counts
        long totalEmployees = employeeRepository.count();

        long presentToday = attendanceRepository.countByDateAndStatus(
                today,
                AttendanceStatus.PRESENT
        );

        long lateToday = attendanceRepository.countByDateAndStatus(
                today,
                AttendanceStatus.LATE
        );

        long absentToday = totalEmployees - (presentToday + lateToday);

        // Add data to model
        model.addAttribute("attendanceList", attendanceList);
        model.addAttribute("today", today);
        model.addAttribute("totalEmployees", totalEmployees);
        model.addAttribute("presentToday", presentToday);
        model.addAttribute("lateToday", lateToday);
        model.addAttribute("absentToday", absentToday);
        model.addAttribute("selectedFilter", filter);

        return "adminAttendance";
    }
}
