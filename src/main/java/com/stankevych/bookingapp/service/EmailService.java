package com.stankevych.bookingapp.service;

public interface EmailService {
    void sendEmail(String to, String subject, String text);
}
