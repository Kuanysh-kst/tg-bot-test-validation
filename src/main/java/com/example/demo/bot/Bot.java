package com.example.demo.bot;

import com.example.demo.config.BotConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

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

            try {
                SendMessage message = createWebAppButtonMessage(
                        chatId,
                        "Нажми кнопку, чтобы открыть WebApp",
                        "https://web-site-frontend-test-task.vercel.app"  // замени на свой URL
                );
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

    private SendMessage createWebAppButtonMessage(long chatId, String text, String webAppUrl) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);

        InlineKeyboardButton webAppButton = new InlineKeyboardButton();
        webAppButton.setText("Открыть WebApp");
        webAppButton.setWebApp(new org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo(webAppUrl));

        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
        keyboardButtonsRow.add(webAppButton);

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(keyboardButtonsRow);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(keyboard);

        message.setReplyMarkup(inlineKeyboardMarkup);

        return message;
    }
}
