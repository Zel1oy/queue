package com.example.queue.config;

import com.example.queue.bot.QueueBot;
import com.example.queue.exception.NotificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class BotConfig {
    @Value(value = "${arsen.telegram.token}")
    private String arsenBotToken;

    @Bean
    public QueueBot arsenTelegramBot() {
        return new QueueBot(arsenBotToken);
    }

    @EventListener({ContextRefreshedEvent.class})
    public void init() {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(arsenTelegramBot());
        } catch (TelegramApiException e) {
            throw new NotificationException("Can't initialize bot ", e);
        }
    }
}
