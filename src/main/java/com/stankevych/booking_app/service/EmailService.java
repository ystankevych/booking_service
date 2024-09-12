package com.stankevych.booking_app.service;

public interface EmailService {
    void sendEmail(String to, String subject, String text);
}
