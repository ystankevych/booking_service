package com.stankevych.bookingapp.config;

import com.stankevych.bookingapp.component.Bot;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@RequiredArgsConstructor
@Configuration
public class BotConfig {
    private final Bot bot;

    @PostConstruct
    private void init() {
        try {
            TelegramBotsApi tba = new TelegramBotsApi(DefaultBotSession.class);
            tba.registerBot(bot);
        } catch (TelegramApiException e) {
            throw new RuntimeException("Registration telegram bot failed", e);
        }
    }
}
