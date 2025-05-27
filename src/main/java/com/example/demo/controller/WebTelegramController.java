package com.example.demo.controller;

import com.example.demo.dto.InitDataRequest;

import com.example.demo.model.MyUser;
import com.example.demo.service.MyUserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class WebTelegramController {
    private final MyUserService userService;
    @Value("${bot.token}")
    private String botToken;

    @PostMapping("/verify")
    public ResponseEntity<?> verifyInitData(@RequestBody InitDataRequest initDataRequest,
                                           HttpSession session) throws Exception {
        MyUser user = userService.handleInitData(initDataRequest, botToken);
        session.setAttribute("user", user);
        Map<String, Object> response = new HashMap<>();
        response.put("status", "ok");
        response.put("message", "hash successful verified!");
        response.put("user", Map.of(
                "id", user.getId(),
                "firstName", user.getFirstName(),
                "lastName", user.getLastName(),
                "username", user.getUsername(),
                "lastLogin", user.getLastLogin() // или toString(), если нужен формат
        ));

        return ResponseEntity.ok(response);
    }
}
