package com.stankevych.booking_app.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "payments")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Payment {
    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.PENDING;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id", nullable = false,
    foreignKey = @ForeignKey(name = "payments_bookings_fk"))
    private Booking booking;

    @Column(nullable = false)
    private String sessionUrl;

    @Column(nullable = false)
    private String sessionId;

    @Column(name = "amount_to_pay", nullable = false)
    private BigDecimal amountToPay;

    public Payment(Booking booking) {
        this.booking = booking;
        amountToPay = calculateTotalAmount();
    }

    private BigDecimal calculateTotalAmount() {
        long countDays = ChronoUnit.DAYS.between(booking.getCheckInDate(),
                booking.getCheckOutDate());
        return booking.getAccommodation().getDailyRate()
                .multiply(BigDecimal.valueOf(countDays));
    }

    public enum Status {
        PENDING, PAID
    }
}
