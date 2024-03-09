package com.stankevych.booking_app.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@Entity(name = "accommodations")
public class Accommodation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String size;

    @ElementCollection(targetClass = String.class)
    @CollectionTable(name = "amenities", joinColumns = @JoinColumn(name = "accommodation_id", nullable = false),
    foreignKey = @ForeignKey(name = "accommodations_amenities_fk"))
    @Column(nullable = false)
    private List<String> amenities = new ArrayList<>();

    @Column(nullable = false)
    private BigDecimal dailyRate;

    @Column(nullable = false)
    private Integer availability;

    @OneToMany(mappedBy = "accommodation", cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true)
    private List<Booking> bookings = new ArrayList<>();

    private enum Type {
        HOUSE, APARTMENT, CONDO, VACATION_HOME
    }
}
