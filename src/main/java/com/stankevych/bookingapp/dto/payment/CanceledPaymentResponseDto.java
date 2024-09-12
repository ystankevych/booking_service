package com.stankevych.bookingapp.dto.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public record CanceledPaymentResponseDto(
        Long bookingId,
        @JsonProperty("pay_until")
        LocalDateTime unpaidTerm) {
}
