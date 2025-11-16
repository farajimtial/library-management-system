package com.library.lms.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DashboardController {

    @GetMapping("/admin/dashboard")
    public String admin() {
        return "Admin Dashboard (placeholder)";
    }

    @GetMapping("/student/dashboard")
    public String student() {
        return "Student Dashboard (placeholder)";
    }

    @GetMapping("/librarian/dashboard")
    public String librarian() {
        return "Librarian Dashboard (placeholder)";
    }
}
