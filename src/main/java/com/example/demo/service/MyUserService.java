package com.example.demo.service;

import com.example.demo.dto.ApiErrorResponse;
import com.example.demo.dto.InitDataRequest;
import com.example.demo.exception.InvalidHashException;
import com.example.demo.model.MyUser;
import com.example.demo.repository.MyUserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyUserService {
    private final MyUserRepository repository;

    @Transactional
    public ApiErrorResponse handleInitData(InitDataRequest request, String botToken) throws Exception {
        Map<String, String> dataMap = parseInitData(request.getInitData());

        String receivedHash = dataMap.remove("hash");

        String timestamp = dataMap.get("auth_date");
        long date = Long.parseLong(timestamp);

        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(date), ZoneId.systemDefault());
        String checkString = dataMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("\n"));

        String secretKey = hmacSha256(botToken);

        String calculatedHash = hmacSha256Hex(secretKey, checkString);

        log.info("Received initData: {}", receivedHash);
        log.info("Calculated initData: {}", calculatedHash);
        if (calculatedHash.equals(receivedHash)) {
            String userData = dataMap.get("user");
            Map<String, String> userMap = jsonToMap(userData);

            long chatId = Long.parseLong(userMap.get("id"));
            String firstName = userMap.get("first_name");
            String lastName = userMap.get("last_name");
            String userName = userMap.get("username");

            MyUser user = repository.findById(chatId)
                    .orElse(new MyUser()); // если нет - создаем нового

            user.setId(chatId);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setUsername(lastName);
            user.setUsername(userName);
            user.setLastLogin(LocalDateTime.now());
            user.setLastLogin(dateTime);

            repository.save(user);
            return new ApiErrorResponse("hash successful verified!", "ok", HttpStatus.OK.value());
        } else {
            throw new InvalidHashException("verification hash doesn't match");
        }
    }

    private Map<String, String> parseInitData(String initData) {
        Map<String, String> map = new HashMap<>();
        for (String pair : initData.split("&")) {
            String[] parts = pair.split("=", 2);
            String key = URLDecoder.decode(parts[0], StandardCharsets.UTF_8);
            String value = parts.length > 1 ? URLDecoder.decode(parts[1], StandardCharsets.UTF_8) : "";
            map.put(key, value);
        }
        return map;
    }

    private String hmacSha256(String data) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec("WebAppData".getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(secretKeySpec);
        return Base64.getEncoder().encodeToString(mac.doFinal(data.getBytes(StandardCharsets.UTF_8)));
    }

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

    public Map<String, String> jsonToMap(String jsonString) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> rawMap = objectMapper.readValue(jsonString, new TypeReference<>() {
        });

        Map<String, String> stringMap = new HashMap<>();
        for (Map.Entry<String, Object> entry : rawMap.entrySet()) {
            stringMap.put(entry.getKey(), String.valueOf(entry.getValue()));
        }

        return stringMap;
    }
}
