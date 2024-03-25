package com.stankevych.booking_app.repository;

import com.stankevych.booking_app.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findAllByBookingUserId(Long userId);

    Optional<Payment> findBySessionIdAndStatusIs(String SessionId,
                                                      Payment.Status status);
}
