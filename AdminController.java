package com.pooja.employee_management_system.controller;

import com.pooja.employee_management_system.entity.LeaveStatus;
import com.pooja.employee_management_system.service.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private LeaveService leaveService;


    // ==========================
    // Admin Dashboard
    // ==========================

    @GetMapping("/dashboard")
    public String dashboard() {

        return "adminDashboard";
    }


    // ==========================
    // Manage Leave
    // ==========================

    @GetMapping("/leaveRequests")
    public String leaveRequests(
            @RequestParam(value = "status", defaultValue = "PENDING") String status,
            Model model) {

        // ==========================
        // Select Leave List
        // ==========================

        if (status.equalsIgnoreCase("ALL")) {

            model.addAttribute(
                    "leaveList",
                    leaveService.getAllLeaves()
            );

        } else if (status.equalsIgnoreCase("APPROVED")) {

            model.addAttribute(
                    "leaveList",
                    leaveService.getApprovedLeaves()
            );

        } else if (status.equalsIgnoreCase("REJECTED")) {

            model.addAttribute(
                    "leaveList",
                    leaveService.getRejectedLeaves()
            );

        } else {

            // Default = Pending
            model.addAttribute(
                    "leaveList",
                    leaveService.getAllPendingLeaves()
            );

            status = "PENDING";
        }


        // ==========================
        // Counts
        // ==========================

        model.addAttribute(
                "pendingCount",
                leaveService.getAllPendingLeaves().size()
        );

        model.addAttribute(
                "approvedCount",
                leaveService.getApprovedLeaves().size()
        );

        model.addAttribute(
                "rejectedCount",
                leaveService.getRejectedLeaves().size()
        );

        model.addAttribute(
                "allCount",
                leaveService.getAllLeaves().size()
        );


        // Current selected filter

        model.addAttribute(
                "selectedStatus",
                status.toUpperCase()
        );


        return "adminLeaveRequests";
    }


    // ==========================
    // Approve Leave
    // ==========================

    @GetMapping("/leave/approve/{id}")
    public String approveLeave(@PathVariable Long id) {

        leaveService.approveLeave(id);

        return "redirect:/admin/leaveRequests";
    }


    // ==========================
    // Reject Leave
    // ==========================

    @GetMapping("/leave/reject/{id}")
    public String rejectLeave(@PathVariable Long id) {

        leaveService.rejectLeave(id);

        return "redirect:/admin/leaveRequests";
    }

}
