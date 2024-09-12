package com.stankevych.bookingapp.service;

import com.stankevych.bookingapp.dto.booking.BookingResponseDto;
import com.stankevych.bookingapp.dto.booking.CreateBookingRequestDto;
import com.stankevych.bookingapp.dto.booking.UpdateBookingRequestDto;
import com.stankevych.bookingapp.model.Booking;
import com.stankevych.bookingapp.model.User;
import java.util.List;

public interface BookingService {
    BookingResponseDto createBooking(User user, CreateBookingRequestDto requestDto);

    BookingResponseDto updateBooking(User user, Long id, UpdateBookingRequestDto booking);

    List<BookingResponseDto> getAllByUserAndStatus(Long userId, Booking.Status status);

    List<BookingResponseDto> getUsersBookings(Long userId);

    void deleteBooking(Long userId, Long bookingId);
}
