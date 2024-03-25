package com.stankevych.booking_app.dto.booking;

import java.time.LocalDate;

public record CreateBookingRequestDto(
        LocalDate checkInDate,
        LocalDate checkOutDate,
        Long accommodationId
) {}
