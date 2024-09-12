package com.stankevych.bookingapp.exception;

public class EmailNotificationException extends RuntimeException {
    public EmailNotificationException(String message) {
        super(message);
    }
}
