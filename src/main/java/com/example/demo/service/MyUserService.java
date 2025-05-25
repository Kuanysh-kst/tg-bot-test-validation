package com.example.demo.service;

import org.springframework.beans.factory.annotation.Value;

public class MyUserService {
    @Value("${telegram.bot.token}")
    private String botToken;

    public boolean validateInitData(String initData) {
        return false;
   }

}
