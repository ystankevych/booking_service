package com.stankevych.bookingapp.dto.booking;

import java.time.LocalDate;

public record BookingResponseDto(
        Long bookingId,
        LocalDate checkInDate,
        LocalDate checkOutDate,
        Long accommodationId,
        Long userId,
        String status) {
}
