package com.example.demo.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

public class HomeController {
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("message", "Привет из Thymeleaf!");
        return "index";  // имя шаблона: src/main/resources/templates/index.html
    }
}
