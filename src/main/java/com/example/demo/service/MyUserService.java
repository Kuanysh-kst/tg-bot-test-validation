package com.example.demo.service;

import com.example.demo.dto.InitDataRequest;
import com.example.demo.repository.MyUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyUserService {
    private final MyUserRepository userRepository;
    @Value("${bot.token}")
    private String botToken;

    @Transactional
    public void handleInitData(InitDataRequest data) throws Exception {

    }
}
