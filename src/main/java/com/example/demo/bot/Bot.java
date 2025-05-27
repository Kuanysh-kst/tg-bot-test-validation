package com.example.demo.bot;

import com.example.demo.config.BotConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
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
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();

            String instructionText = """
                Для настройки WebApp выполните следующие шаги:
                1. Разверните WebApp (локально через ngrok).
                2. Закиньте url ngrok в botFather.
                3. Нажмите кнопку для открытия WebApp.
                """;

            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));
            message.setText(instructionText);

            try {
                execute(message);
            } catch (TelegramApiException e) {
                log.error("Ошибка при отправке сообщения", e);
            }
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
