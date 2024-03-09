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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Getter;
import lombok.ToString;

@Entity
@Table(name = "bookings")
@Getter
@ToString
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "check_in_date", nullable = false)
    private LocalDate checkInDate;

    @Column(name = "check_ot_date", nullable = false)
    private LocalDate checkOutDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "accommodation_id", nullable = false,
    foreignKey = @ForeignKey(name = "bookings_accommodations_fk"))
    private Accommodation accommodation;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false,
    foreignKey = @ForeignKey(name = "bookings_users_fk"))
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    private enum Status {
        PENDING, CONFIRMED, CANCELED, EXPIRED
    }
}
