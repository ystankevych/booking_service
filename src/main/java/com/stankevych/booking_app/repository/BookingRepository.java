package com.stankevych.booking_app.repository;

import com.stankevych.booking_app.model.Booking;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("""
            SELECT COUNT(b.id) FROM Booking b WHERE b.accommodation.id = :accommodationId
            AND b.checkInDate < :checkOutDate AND b.checkOutDate > :checkInDate""")
    int countBookingsOnDate(@Param("accommodationId") Long accommodationId,
                            @Param("checkInDate") LocalDate checkinDate,
                            @Param("checkOutDate") LocalDate checkoutDate);

    boolean existsByUserIdAndStatusIs(Long userId, Booking.Status status);

    List<Booking> findAllByUserIdAndStatus(Long userId, Booking.Status status);

    List<Booking> findAllByUserId(Long userId);

    @EntityGraph(attributePaths = "payment")
    Optional<Booking> findByIdAndUserIdAndStatusIs(Long id, Long userId, Booking.Status status);

    List<Booking> findAllByUnpaidTermIsLessThanEqual(LocalDateTime localDateTime);
}
