package com.stankevych.booking_app.service;

import com.stankevych.booking_app.dto.payment.PaymentDtoWithoutSession;
import com.stankevych.booking_app.dto.payment.PaymentRequestDto;
import com.stankevych.booking_app.dto.payment.PaymentResponseDto;
import com.stankevych.booking_app.model.User;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

public interface PaymentService {
    PaymentResponseDto createPayment(Long bookingId, Long userId);

    List<PaymentDtoWithoutSession> findAllByUserId(Long userId);

    PaymentDtoWithoutSession completePayment(String sessionId);

    PaymentDtoWithoutSession cancelPayment(String sessionId);
}
