package com.stankevych.booking_app.dto.booking;

import java.time.LocalDate;

public record UpdateBookingRequestDto(
        LocalDate checkInDate,
        LocalDate checkOutDate
) {}
