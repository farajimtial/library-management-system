package com.library.lms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * HomeController - Redirects root to login
 */
@Controller
public class HomeController {
    
    @GetMapping("/")
    public String home() {
        return "redirect:/auth/login";
    }
}