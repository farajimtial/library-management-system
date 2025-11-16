package com.library.lms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RootController {
    @GetMapping("/")
    public String home() {
        return "redirect:/auth/login";
    }
    
    @GetMapping("/test")
    @ResponseBody
    public String test() {
        return "✅ Server is working! LMS Application is running on port 8080.";
    }
}
