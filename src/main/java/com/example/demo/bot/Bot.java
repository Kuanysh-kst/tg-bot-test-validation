package com.example.demo.bot;

import com.example.demo.config.BotConfig;
import com.example.demo.util.SendModels;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class Bot extends TelegramLongPollingBot {
    @Autowired
    private final BotConfig botConfig;

    public Bot(BotConfig botConfig) {
        this.botConfig = botConfig;
    }

    @Override
    public void onUpdateReceived(Update update) {
        long chatId = update.getMessage().getChat().getId();
        String id = String.valueOf(chatId);
        log.info("it is working!");

        try {
            Message response = execute(SendModels.sendText(chatId, "Hello!" + id));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotUserName();
    }
    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

}
