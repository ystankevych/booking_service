package com.stankevych.booking_app.service.impl;

import com.stankevych.booking_app.model.Booking;
import com.stankevych.booking_app.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SchedulerService {
    private final BookingRepository bookingRepository;

    @Scheduled(cron = "0 */15 * * * ?")
    private void cancelUnpaidBooking() {
        List<Booking> list = bookingRepository
                .findAllByUnpaidTermIsLessThanEqual(LocalDateTime.now())
                .stream()
                .peek(b -> b.setStatus(Booking.Status.CANCELED))
                .toList();
        bookingRepository.saveAll(list);
    }
}
