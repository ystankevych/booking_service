package com.stankevych.booking_app.dto.accommodation;

import java.math.BigDecimal;
import java.util.List;

public record AccommodationResponseDto(
        Long id,
        String type,
        String address,
        String size,
        List<String> amenities,
        BigDecimal dailyRate,
        Integer availability) {
}
