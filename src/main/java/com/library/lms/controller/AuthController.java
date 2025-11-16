package com.library.lms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.library.lms.entity.Student;
import com.library.lms.entity.User;
import com.library.lms.enums.Role;
import com.library.lms.service.UserService;

import jakarta.servlet.http.HttpSession;

/**
 * AuthController - Handles authentication and user registration
 * Default Admin: username = "admin", password = "admin"
 */
@Controller
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    private UserService userService;
    
    /**
     * Show login page
     */
    @GetMapping("/login")
    public String showLoginPage(@RequestParam(required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid username or password");
        }
        return "login";
    }
    
    /**
     * Handle login request
     * Special handling for default admin account
     */
    @PostMapping("/login")
    public String login(@RequestParam String username, 
                       @RequestParam String password,
                       HttpSession session,
                       Model model) {
        try {
            // Check for default admin login
            if ("admin".equals(username) && "admin".equals(password)) {
                User adminUser = new User();
                adminUser.setUserId(0L);
                adminUser.setUsername("admin");
                adminUser.setEmail("admin@library.com");
                adminUser.setRole(Role.ADMIN);
                
                session.setAttribute("user", adminUser);
                session.setAttribute("userId", 0L);
                session.setAttribute("username", "admin");
                session.setAttribute("role", Role.ADMIN);
                
                return "redirect:/admin/dashboard";
            }
            
            // Regular user login
            User user = userService.login(username, password);
            
            if (user != null) {
                session.setAttribute("user", user);
                session.setAttribute("userId", user.getUserId());
                session.setAttribute("username", user.getUsername());
                session.setAttribute("role", user.getRole());
                
                Role role = user.getRole();
                if (role != null) {
                    switch (role) {
                        case STUDENT:
                            return "redirect:/student/dashboard";
                        case LIBRARIAN:
                            return "redirect:/librarian/dashboard";
                        case ADMIN:
                            return "redirect:/admin/dashboard";
                        default:
                            break;
                    }
                }
            }
            
            model.addAttribute("error", "Invalid username or password");
            return "login";
            
        } catch (Exception e) {
            model.addAttribute("error", "Login failed: " + e.getMessage());
            return "login";
        }
    }
    
    /**
     * Handle logout
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/auth/login";
    }
    
    /**
     * Show student registration page
     */
    @GetMapping("/register/student")
    public String showStudentRegistration(Model model) {
        model.addAttribute("student", new Student()); // <-- Added this
        return "register-student";
    }
    
    /**
     * Handle student registration
     */
    @PostMapping("/register/student")
    public String registerStudent(@ModelAttribute("student") Student student,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        try {
            userService.registerStudent(student);
            redirectAttributes.addFlashAttribute("success", "Registration successful! Please login.");
            return "redirect:/auth/login";
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "register-student";
        }
    }
}
