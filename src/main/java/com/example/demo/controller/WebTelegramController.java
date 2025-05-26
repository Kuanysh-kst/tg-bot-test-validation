package com.example.demo.controller;

import com.example.demo.dto.InitDataRequest;

import com.example.demo.service.MyUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class WebTelegramController {
    private final MyUserService userService;
    @Value("${bot.token}")
    private String botToken;

    @PostMapping("/verify")
    public ResponseEntity<?> verifyInitData(@RequestBody InitDataRequest initDataRequest) throws Exception {
        return ResponseEntity.ok(userService.handleInitData(initDataRequest));
    }
}
