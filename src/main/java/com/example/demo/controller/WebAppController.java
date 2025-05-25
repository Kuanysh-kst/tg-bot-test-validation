package com.example.demo.controller;

import com.example.demo.dto.InitDataRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class WebAppController {

    @GetMapping("/validate")
    public String index(@RequestBody InitDataRequest request) {
        return "index";
    }
}
