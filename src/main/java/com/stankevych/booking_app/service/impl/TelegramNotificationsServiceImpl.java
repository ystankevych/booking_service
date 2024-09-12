package com.stankevych.booking_app.service.impl;

import com.stankevych.booking_app.component.Bot;
import com.stankevych.booking_app.model.Booking;
import com.stankevych.booking_app.model.User;
import com.stankevych.booking_app.repository.TelegramUserRepository;
import com.stankevych.booking_app.service.TelegramNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TelegramNotificationsServiceImpl implements TelegramNotificationService {
    private final Bot bot;
    private final TelegramUserRepository repository;


    @Override
    public void notifyAboutCreatedBooking(Booking booking) {
        notifyUser(booking.getUser(), """
                Hi, %s!
                You successfully booked accommodation.
                Please, check information about your booking:
                %s"""
                .formatted(booking.getUser().getFirstName(),
                        bookingInfo(booking)));
    }

    @Override
    public void notifyAboutAvailableBookings(List<Booking> bookings) {
        if (!bookings.isEmpty()) {
            notifyAll(bookingsAvailabilityMessage(bookings));
        }
    }

    private String bookingInfo(Booking booking) {
        return """
                🏡
                🔹type: %s
                🔹address: %s
                🔹check in date: %s
                🔹check out date: %s"""
                .formatted(booking.getAccommodation().getType(),
                        booking.getAccommodation().getAddress(),
                        booking.getCheckInDate(),
                        booking.getCheckOutDate());
    }

    private String bookingsAvailabilityMessage(List<Booking> bookings) {
        return """
                Hi!
                We have good news for you =)
                The following bookings are available from today ⬇️
                Hurry up and book first ⚡️"""
                + bookings.stream()
                .map(b -> """
                        %n🏡
                        🔹type: %s
                        🔹address: %s"""
                        .formatted(b.getAccommodation().getType(),
                                b.getAccommodation().getAddress()))
                .collect(Collectors.joining("\n"));
    }

    private void notifyUser(User user, String message) {
        repository.findByUserIdAndIsActive(user.getId(), true)
                .ifPresent(tu -> bot.notifyUser(message, tu.getId()));
    }

    private void notifyAll(String message) {
        repository.findAllByIsActive(true)
                .forEach(tu -> bot.notifyUser(message, tu.getId()));
    }
}
