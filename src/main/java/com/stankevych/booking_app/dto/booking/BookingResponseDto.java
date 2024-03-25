package com.stankevych.booking_app.dto.booking;

import java.time.LocalDate;

public record BookingResponseDto(
        Long bookingId,
        LocalDate checkInDate,
        LocalDate checkOutDate,
        Long accommodationId,
        Long userId,
        String status
) {}
