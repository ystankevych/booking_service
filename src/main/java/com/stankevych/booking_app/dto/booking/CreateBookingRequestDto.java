package com.stankevych.booking_app.dto.booking;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

public record CreateBookingRequestDto(
        @FutureOrPresent(message = "Check-in date must be now or in the future")
        LocalDate checkInDate,

        @Future(message = "Check-out date must be in the future")
        LocalDate checkOutDate,

        @Positive(message = "Accommodation id must be positive")
        @NotNull(message = "Accommodation id must not be null")
        Long accommodationId
) {
}
