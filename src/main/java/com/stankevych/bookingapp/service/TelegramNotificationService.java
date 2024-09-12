package com.stankevych.bookingapp.service;

import com.stankevych.bookingapp.model.Booking;
import java.util.List;

public interface TelegramNotificationService {
    void notifyAboutCreatedBooking(Booking booking);

    void notifyAboutAvailableBookings(List<Booking> bookings);
}
