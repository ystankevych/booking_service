package com.stankevych.booking_app.dto.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public record PaymentDtoWithoutSession(
        @JsonProperty("id_booking_id")
        Long id,
        String status,
        @JsonProperty("total_amount")
        BigDecimal amountToPay) {
}
