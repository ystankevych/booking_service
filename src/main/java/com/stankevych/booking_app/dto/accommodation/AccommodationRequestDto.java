package com.stankevych.booking_app.dto.accommodation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.List;

public record AccommodationRequestDto(
        @NotBlank(message = "Type must not be null or empty")
        String type,

        @NotBlank(message = "Address must not be null or empty")
        String address,

        @NotBlank(message = "Size must not be null or empty")
        String size,
        List<String> amenities,

        @Positive(message = "Daily rate must be positive")
        @NotNull(message = "Daily rate must not be null")
        BigDecimal dailyRate,

        @Positive(message = "Availability must be positive")
        @NotNull(message = "Availability must not be null")
        Integer availability) {
}
