package com.stankevych.booking_app.exception;

public class EmailNotificationException extends RuntimeException {
    public EmailNotificationException(String message) {
        super(message);
    }
}
