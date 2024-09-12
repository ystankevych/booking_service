package com.stankevych.booking_app.service;

import com.stankevych.booking_app.model.Booking;

import java.util.List;

public interface TelegramNotificationService {
    void notifyAboutCreatedBooking(Booking booking);

    void notifyAboutAvailableBookings(List<Booking> bookings);
}
