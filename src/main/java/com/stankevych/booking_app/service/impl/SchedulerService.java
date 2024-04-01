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
    private final StripeServiceImpl stripeService;

    @Scheduled(cron = "0 */2 * * * *")
    private void cancelUnpaidBooking() {
        List<Booking> list = bookingRepository
                .findAllByUnpaidTermIsLessThanEqualAndStatusIs(LocalDateTime.now(), Booking.Status.PENDING)
                .stream()
                .peek(b -> {
                    if (b.getPayment() != null) {
                        stripeService.expireSession(b.getPayment().getSessionId());
                    }
                })
                .toList();
        bookingRepository.deleteAll(list);
    }
}
