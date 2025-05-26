package com.example.demo.controller;

import com.example.demo.dto.InitDataRequest;

import com.example.demo.service.MyUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<String> verifyInitData(@RequestBody InitDataRequest request) {
        try {
            // 1. Парсим строку в карту
            Map<String, String> dataMap = parseInitData(request.getInitData());

            // 2. Получаем оригинальный hash
            String receivedHash = dataMap.remove("hash");

            // 3. Формируем строку проверки данных
            String checkString = dataMap.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .map(entry -> entry.getKey() + "=" + entry.getValue())
                    .collect(Collectors.joining("\n"));

            // 4. Вычисляем секретный ключ
            String secretKey = hmacSha256("WebAppData", botToken);

            // 5. Считаем HMAC-SHA-256 по строке
            String calculatedHash = hmacSha256Hex(secretKey, checkString);

            // 6. Сравниваем
            if (calculatedHash.equals(receivedHash)) {
                return ResponseEntity.ok("✅ Проверка пройдена успешно");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("❌ Ошибка верификации: hash не совпадает");
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("❌ Ошибка сервера: " + e.getMessage());
        }
    }

    // Метод разбора строки
    private Map<String, String> parseInitData(String initData) throws UnsupportedEncodingException {
        Map<String, String> map = new HashMap<>();
        for (String pair : initData.split("&")) {
            String[] parts = pair.split("=", 2);
            String key = URLDecoder.decode(parts[0], StandardCharsets.UTF_8);
            String value = parts.length > 1 ? URLDecoder.decode(parts[1], StandardCharsets.UTF_8) : "";
            map.put(key, value);
        }
        return map;
    }

    // Вычисление HMAC (base64)
    private String hmacSha256(String key, String data) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(secretKeySpec);
        return Base64.getEncoder().encodeToString(mac.doFinal(data.getBytes(StandardCharsets.UTF_8)));
    }

    // Вычисление HMAC в hex
    private String hmacSha256Hex(String keyBase64, String data) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(keyBase64);
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(secretKeySpec);
        byte[] rawHmac = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : rawHmac) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
