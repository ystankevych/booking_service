package com.stankevych.bookingapp.controller;

import com.stankevych.bookingapp.dto.booking.BookingResponseDto;
import com.stankevych.bookingapp.dto.booking.CreateBookingRequestDto;
import com.stankevych.bookingapp.model.Booking;
import com.stankevych.bookingapp.model.User;
import com.stankevych.bookingapp.service.BookingService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponseDto createBooking(@RequestBody @Valid CreateBookingRequestDto requestDto,
                                            Authentication authentication) {
        var user = (User) authentication.getPrincipal();
        return bookingService.createBooking(user, requestDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBooking(@PathVariable @Positive Long id,
                              Authentication authentication) {
        var user = (User) authentication.getPrincipal();
        bookingService.deleteBooking(user.getId(), id);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public List<BookingResponseDto> getAllByUserAndStatus(
            @RequestParam(name = "user_id") @Positive Long userId,
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
