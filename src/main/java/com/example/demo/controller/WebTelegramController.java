package com.example.demo.controller;

import com.example.demo.dto.InitDataRequest;

import com.example.demo.exception.InvalidHashException;
import com.example.demo.service.MyUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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
