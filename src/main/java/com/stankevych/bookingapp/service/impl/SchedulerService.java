package com.stankevych.bookingapp.service.impl;

import com.stankevych.bookingapp.model.Booking;
import com.stankevych.bookingapp.repository.BookingRepository;
import com.stankevych.bookingapp.service.PaymentService;
import com.stankevych.bookingapp.service.TelegramNotificationService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SchedulerService {
    private final BookingRepository bookingRepository;
    private final PaymentService paymentService;
    private final TelegramNotificationService notificationService;

    @Scheduled(cron = "0 */20 * * * *")
    private void cancelUnpaidBooking() {
        var bookings = bookingRepository
                .findAllByUnpaidTermIsLessThanEqualAndStatusIs(LocalDateTime.now(),
                        Booking.Status.PENDING);

        bookings.stream()
                .map(Booking::getPayment)
                .filter(Objects::nonNull)
                .forEach(paymentService::expirePaymentSession);

        bookingRepository.deleteAll(bookings);
    }

    @Scheduled(cron = "0 */2 * * * *")
    private void getExpiredBooking() {
        var bookings = bookingRepository.findAllByCheckOutDateEquals(LocalDate.now().plusDays(2));
        bookings.forEach(b -> {
            b.setStatus(Booking.Status.EXPIRED);
        });
        notificationService.notifyAboutAvailableBookings(bookings);
    }
}
