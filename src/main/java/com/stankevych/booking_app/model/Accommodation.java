package com.stankevych.booking_app.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "accommodations")
@Getter
@Setter
@ToString(exclude = "bookings")
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

    @OneToMany(mappedBy = "accommodation", cascade = CascadeType.REMOVE,
            orphanRemoval = true)
    private List<Booking> bookings = new ArrayList<>();

    @Getter
    public enum Type {
        HOUSE("House"),
        APARTMENT("Apartment"),
        CONDO("Condo"),
        VACATION_HOME("Vacation home");

        private String typeName;

        Type(String typeName) {
            this.typeName = typeName;
        }

    }
}

