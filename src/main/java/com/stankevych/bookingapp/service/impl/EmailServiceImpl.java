package com.stankevych.bookingapp.service.impl;

import com.stankevych.bookingapp.exception.EmailNotificationException;
import com.stankevych.bookingapp.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender sender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void sendEmail(String to, String subject, String text) {
        MimeMessage mimeMessage = sender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);
            sender.send(mimeMessage);
        } catch (Exception e) {
            throw new EmailNotificationException("Can't send email to the email '%s'"
                    .formatted(to));
        }
    }
}
