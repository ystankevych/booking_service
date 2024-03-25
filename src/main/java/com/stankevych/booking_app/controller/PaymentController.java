package com.stankevych.booking_app.controller;

import com.stankevych.booking_app.dto.payment.PaymentDtoWithoutSession;
import com.stankevych.booking_app.dto.payment.PaymentRequestDto;
import com.stankevych.booking_app.dto.payment.PaymentResponseDto;
import com.stankevych.booking_app.model.User;
import com.stankevych.booking_app.service.PaymentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public PaymentResponseDto createPayment(@RequestBody PaymentRequestDto requestDto,
                                            Authentication authentication) {
        var user = (User) authentication.getPrincipal();
        return paymentService
                .createPayment(requestDto.bookingId(), user.getId());
    }

    @GetMapping("/success")
    @PreAuthorize("hasRole('CUSTOMER')")
    public PaymentDtoWithoutSession completePayment(@RequestParam String sessionId) {
        return paymentService.completePayment(sessionId);
    }

    @GetMapping("/cancel")
    @PreAuthorize("hasRole('CUSTOMER')")
    public PaymentDtoWithoutSession cancelPayment(@RequestParam String sessionId) {
        return paymentService.cancelPayment(sessionId);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER') or #userId.equals(authentication.principal.id)")
    public List<PaymentDtoWithoutSession> getAllByUserId(@RequestParam(name = "user_id") Long userId) {
        return paymentService.findAllByUserId(userId);
    }
}
