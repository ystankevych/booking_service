package com.stankevych.booking_app.controller;

import com.stankevych.booking_app.dto.payment.CanceledPaymentResponseDto;
import com.stankevych.booking_app.dto.payment.PaymentDtoWithoutSession;
import com.stankevych.booking_app.dto.payment.PaymentRequestDto;
import com.stankevych.booking_app.dto.payment.PaymentResponseDto;
import com.stankevych.booking_app.model.User;
import com.stankevych.booking_app.service.PaymentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
@Validated
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentResponseDto createPayment(@RequestBody @Valid PaymentRequestDto requestDto,
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
    public CanceledPaymentResponseDto cancelPayment(@RequestParam String sessionId) {
        return paymentService.cancelPayment(sessionId);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER') or #userId.equals(authentication.principal.id)")
    public List<PaymentDtoWithoutSession> getAllByUserId(
            @RequestParam(name = "user_id") @Positive Long userId) {
        return paymentService.findAllByUserId(userId);
    }
}
