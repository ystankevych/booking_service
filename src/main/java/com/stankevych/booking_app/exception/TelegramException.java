package com.stankevych.booking_app.exception;

public class TelegramException extends RuntimeException {
    public TelegramException(String message) {
        super(message);
    }
}
