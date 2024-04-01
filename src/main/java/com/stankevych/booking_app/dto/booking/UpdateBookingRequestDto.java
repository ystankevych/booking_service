package com.stankevych.booking_app.dto.booking;

import com.stankevych.booking_app.validation.date.Date;
import java.time.LocalDate;

@Date(fields = {"checkInDate", "checkOutDate"},
        message = "Check-in date must be before check-out date")
public record UpdateBookingRequestDto(
        LocalDate checkInDate,
        LocalDate checkOutDate) {
}
