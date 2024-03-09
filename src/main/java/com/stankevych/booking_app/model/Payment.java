package com.stankevych.booking_app.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.ToString;

@Entity
@Table(name = "payments")
@Getter
@ToString
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "booking_id", nullable = false,
    foreignKey = @ForeignKey(name = "payments_bookings_fk"))
    private Booking booking;

    @Column(nullable = false)
    private String sessionUrl;

    @Column(nullable = false)
    private String sessionId;

    @Column(name = "amount_to_pay", nullable = false)
    private BigDecimal amountToPay;

    private enum Status {
        PENDING, PAID
    }
}
