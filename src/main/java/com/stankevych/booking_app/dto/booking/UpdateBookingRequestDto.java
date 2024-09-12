package com.stankevych.booking_app.dto.booking;

import com.stankevych.booking_app.validation.date.Date;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import java.time.LocalDate;

@Date(fields = {"checkInDate", "checkOutDate"},
        message = "Check-in date must be before check-out date")
public record UpdateBookingRequestDto(
        @FutureOrPresent(message = "Check-in date must be now or in the future")
        LocalDate checkInDate,

        @Future(message = "Check-out date must be in the future")
        LocalDate checkOutDate) {
}
