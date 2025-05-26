package com.example.demo.service;

import com.example.demo.dto.ApiErrorResponse;
import com.example.demo.dto.InitDataRequest;
import com.example.demo.exception.InvalidHashException;
import com.example.demo.repository.MyUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyUserService {
    private final MyUserRepository userRepository;

    @Transactional
    public ApiErrorResponse handleInitData(InitDataRequest request,String botToken) throws Exception {
            Map<String, String> dataMap = parseInitData(request.getInitData());

            String receivedHash = dataMap.remove("hash");

            String checkString = dataMap.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .map(entry -> entry.getKey() + "=" + entry.getValue())
                    .collect(Collectors.joining("\n"));

            String secretKey = hmacSha256(botToken);

            String calculatedHash = hmacSha256Hex(secretKey, checkString);

            if (calculatedHash.equals(receivedHash)) {
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
}
