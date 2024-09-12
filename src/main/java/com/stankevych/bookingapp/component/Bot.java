package com.stankevych.bookingapp.component;

import com.stankevych.bookingapp.exception.TelegramException;
import com.stankevych.bookingapp.model.TelegramUser;
import com.stankevych.bookingapp.repository.TelegramUserRepository;
import com.stankevych.bookingapp.repository.UserRepository;
import com.stankevych.bookingapp.service.EmailService;
import java.util.Random;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@RequiredArgsConstructor
@Getter
public class Bot extends TelegramLongPollingBot {
    private static final String START_COMMAND = "/start";
    private static final String DEACTIVATE_COMMAND = "/deactivate";
    private static final String EMAIL_SUBJECT = "Verification code";
    private static final String EMAIL_BODY = "Your authorization code is: '%s'";
    private final UserRepository userRepository;
    private final TelegramUserRepository telegramRepository;
    private final EmailService emailService;

    @Value("${bot.username}")
    private String botUserName;

    @Value("${bot.token}")
    private String botToken;

    @Override
    public void onUpdateReceived(Update update) {
        final String chatId = String.valueOf(update.getMessage().getChatId());
        if (update.hasMessage() && update.getMessage().hasText()) {
            final String message = update.getMessage().getText();
            switch (message) {
                case START_COMMAND -> handleStartCommand(chatId);
                case DEACTIVATE_COMMAND -> handleDeactivateCommand(chatId);
                default -> handleMessage(message, chatId);
            }
        }
    }

    private void handleStartCommand(String chatId) {
        telegramRepository.save(new TelegramUser(chatId));
        notifyUser("Please, put your Booking service email", chatId);
    }

    private void handleDeactivateCommand(String chatId) {
        telegramRepository.save(new TelegramUser(chatId));
        notifyUser("""
                Your account is deactivated.
                To activate it again put /start""", chatId);
    }

    private void handleMessage(String message, String chatId) {
        TelegramUser user = telegramRepository.findById(chatId)
                .orElseGet(() -> new TelegramUser(chatId));
        if (user.getUser() == null) {
            sendEmail(message, user);
        } else {
            if (check(message, user)) {
                activate(user);
                return;
            }
            notifyUser("Invalid code", chatId);
        }
    }

    private void sendEmail(String email, TelegramUser telegramUser) {
        var user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            notifyUser("Incorrect user email", telegramUser.getId());
            return;
        }
        String validationCode = generateValidationCode();
        try {
            emailService.sendEmail(email, EMAIL_SUBJECT, EMAIL_BODY.formatted(validationCode));
            telegramUser.setUser(user.get());
            telegramUser.setValidationCode(validationCode);
            telegramRepository.save(telegramUser);
            notifyUser("""
                    Verification code was sent to your email.
                    Please, put it below ⬇️""", telegramUser.getId());
        } catch (Exception e) {
            notifyUser("""
                    Can't complete verification.
                    Please, try again later""", telegramUser.getId());
        }
    }

    private void activate(TelegramUser user) {
        user.setActive(true);
        telegramRepository.save(user);
        notifyUser("""
                ✅ Hi, %s, your account is activated.
                   In this bot you will get notifications on
                                🔹 created rentals
                                🔹 successful payments
                                🔹 overdue rentals
                                🔹 notifications if you have any overdue rentals            
                                We hope you will enjoy your trip!🌎
                                ❌ To deactivate notifications put /deactivate"""
                .formatted(user.getUser().getFirstName()), user.getId());
    }

    private boolean check(String message, TelegramUser user) {
        return user.getValidationCode().equals(message);
    }

    private String generateValidationCode() {
        final int min = 1000;
        final int max = 10000;
        return String.valueOf(new Random().nextInt(min, max));
    }

    public void notifyUser(String message, String chatId) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(message)
                .build();
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new TelegramException("Sending message failed");
        }
    }

    @Override
    public String getBotUsername() {
        return botUserName;
    }
}
