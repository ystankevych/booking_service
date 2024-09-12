package com.stankevych.bookingapp.dto.payment;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PaymentRequestDto(
        @Positive(message = "Booking id must be positive")
        @NotNull(message = "Booking id must not be null")
        Long bookingId) {
}
