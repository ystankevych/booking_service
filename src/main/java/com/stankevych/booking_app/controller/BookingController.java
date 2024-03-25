package com.stankevych.booking_app.controller;

import com.stankevych.booking_app.dto.booking.BookingResponseDto;
import com.stankevych.booking_app.dto.booking.CreateBookingRequestDto;
import com.stankevych.booking_app.model.Booking;
import com.stankevych.booking_app.model.User;
import com.stankevych.booking_app.service.BookingService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public BookingResponseDto createBooking(@RequestBody CreateBookingRequestDto requestDto,
                                            Authentication authentication) {
        var user = (User) authentication.getPrincipal();
        return bookingService.createBooking(user, requestDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public void deleteBooking(@PathVariable Long id,
                              Authentication authentication) {
        var user = (User) authentication.getPrincipal();
        bookingService.deleteBooking(user.getId(), id);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public List<BookingResponseDto> getAllByUserAndStatus(
            @RequestParam(name = "user_id") Long userId,
            @RequestParam Booking.Status status) {
        return bookingService.getAllByUserAndStatus(userId, status);
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('CUSTOMER')")
    public List<BookingResponseDto> getUsersBookings(Authentication authentication) {
        var user = (User) authentication.getPrincipal();
        return bookingService.getUsersBookings(user.getId());
    }
}
