package com.stankevych.bookingapp.service;

import com.stankevych.bookingapp.dto.payment.CanceledPaymentResponseDto;
import com.stankevych.bookingapp.dto.payment.PaymentDtoWithoutSession;
import com.stankevych.bookingapp.dto.payment.PaymentResponseDto;
import com.stankevych.bookingapp.model.Payment;
import java.util.List;

public interface PaymentService {
    PaymentResponseDto createPayment(Long bookingId, Long userId);

    List<PaymentDtoWithoutSession> findAllByUserId(Long userId);

    PaymentDtoWithoutSession completePayment(String sessionId);

    CanceledPaymentResponseDto cancelPayment(String sessionId);

    void expirePaymentSession(Payment payment);
}
