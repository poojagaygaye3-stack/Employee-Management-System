package com.pooja.employee_management_system.controller;

import com.pooja.employee_management_system.entity.User;
import com.pooja.employee_management_system.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    // Open Home(Login) Page
    @GetMapping("/")
    public String home() {
        return "home";
    }

    // Login Authentication
    @PostMapping("/login")
    public String login(@RequestParam String category,
                        @RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {

        User user = userService.login(category, email, password);

        if (user != null) {

            // Save logged-in user in session
            session.setAttribute("loggedInUser", user);

            if (category.equals("ADMIN")) {
                return "redirect:/admin/dashboard";
            }

            if (category.equals("EMPLOYEE")) {
                return "redirect:/employee/dashboard";
            }
        }

        model.addAttribute("error", "Invalid Email or Password");

        return "home";
    }

    // Logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {

        session.invalidate();

        return "redirect:/";
    }
}
