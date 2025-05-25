package com.example.demo.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@Data
public class BotConfig {
    @Value("${bot.username}")
    String botUserName;

    @Value("${bot.token}")
    String token;

    @Value("${bot.owner}")
    Long ownerId;
}
