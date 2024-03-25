package com.stankevych.booking_app.service;

import com.stankevych.booking_app.dto.booking.BookingResponseDto;
import com.stankevych.booking_app.dto.booking.CreateBookingRequestDto;
import com.stankevych.booking_app.dto.booking.UpdateBookingRequestDto;
import com.stankevych.booking_app.model.Booking;
import com.stankevych.booking_app.model.User;

import java.util.List;

public interface BookingService {
    BookingResponseDto createBooking(User user, CreateBookingRequestDto requestDto);

    List<BookingResponseDto> getAllByUserAndStatus(Long userId, Booking.Status status);

    List<BookingResponseDto> getUsersBookings(Long userId);

    void deleteBooking(Long userId, Long bookingId);
}
