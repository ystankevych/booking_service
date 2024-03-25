package com.stankevych.booking_app.repository;

import com.stankevych.booking_app.model.Booking;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    boolean existsByUserIdAndStatusIs(Long userId, Booking.Status status);

    List<Booking> findAllByUserIdAndStatus(Long userId, Booking.Status status);

    List<Booking> findAllByUserId(Long userId);

    @EntityGraph(attributePaths = "payment")
    Optional<Booking> findByIdAndUserIdAndStatusIs(Long id, Long userId, Booking.Status status);

    @EntityGraph(attributePaths = "payment")
    Optional<Booking> findByIdAndUserId(Long id, Long userId);
}
