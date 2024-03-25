package com.stankevych.booking_app.controller;

import com.stankevych.booking_app.dto.booking.BookingResponseDto;
import com.stankevych.booking_app.dto.booking.CreateBookingRequestDto;
import com.stankevych.booking_app.dto.booking.UpdateBookingRequestDto;
import com.stankevych.booking_app.model.Booking;
import com.stankevych.booking_app.model.User;
import com.stankevych.booking_app.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingResponseDto createBooking(@RequestBody CreateBookingRequestDto requestDto,
                                            Authentication authentication) {
        var user = (User) authentication.getPrincipal();
        return bookingService.createBooking(user, requestDto);
    }

    @DeleteMapping("/{id}")
    public void deleteBooking(@PathVariable Long id,
                              Authentication authentication) {
        var user = (User) authentication.getPrincipal();
        bookingService.deleteBooking(user.getId(), id);
    }

    @GetMapping
    public List<BookingResponseDto> getAllByUserAndStatus(
            @RequestParam(name = "user_id") Long userId,
            @RequestParam Booking.Status status) {
        return bookingService.getAllByUserAndStatus(userId, status);
    }

    @GetMapping("/my")
    public List<BookingResponseDto> getUsersBookings(Authentication authentication) {
        var user = (User) authentication.getPrincipal();
        return bookingService.getUsersBookings(user.getId());
    }
}
