package com.stankevych.bookingapp.dto.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public record PaymentResponseDto(
        @JsonProperty("id_booking_id")
        Long id,
        String status,
        BigDecimal amountToPay,
        String sessionUrl) {
}
