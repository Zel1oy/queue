package com.example.queue.bot;

import com.example.queue.exception.NotificationException;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class QueueBot extends TelegramLongPollingBot {
    private static final String WELCOME_MESSAGE = ". Вітаю у боті для черги зроблений спеціально для ІПЗ-24🤌.";
    private static final String REGISTER_MESSAGE = ", ти зареєстрований(а) в базу данних";
    private static final String START_COMMAND = "/start";
    private static final String REGISTER_COMMAND = "/register";
    private List<BotCommand> commands;

    public QueueBot(String botToken) {
        super(botToken);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText();
            switch (message) {
                case (START_COMMAND) -> startCommandReceived(update);
                case (REGISTER_COMMAND) -> registerCommandReceived(update);
            }
        }
    }

    @PostConstruct
    private void initMenu() {
        commands = new ArrayList<>();
        commands.add(new BotCommand(START_COMMAND, "send a welcome message"));
        commands.add(new BotCommand(REGISTER_COMMAND, "register a new student"));
        try {
            this.execute(new SetMyCommands(commands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            throw new NotificationException("Can't initialize command menu", e);
        }
    }

    @Override
    public String getBotUsername() {
        return "queue_ipz_bot";
    }

    private void registerCommandReceived(Update update) {
        Long chatId = update.getMessage().getChatId();
        sendMessage(chatId, "Увведіть своє ім'я та прізвище (e.g. Ім'я Фамілія)");
        String answer = update.getMessage().getChat().getFirstName()
                + REGISTER_MESSAGE;
        sendMessage(update.getMessage().getChatId(), answer);
    }

    private void startCommandReceived(Update update) {
        String answer = "Привіт, " + update.getMessage().getChat().getFirstName() + WELCOME_MESSAGE;
        sendMessage(update.getMessage().getChatId(), answer);
    }

    private void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new NotificationException("Can't send message. PLease, try again" + e);
        }
    }
}
