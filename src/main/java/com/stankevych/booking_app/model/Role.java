package com.stankevych.booking_app.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

@Entity
@Table(name = "roles")
@Getter
@ToString
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleName roleName;

    private enum RoleName{
        ROLE_ADMIN,
        ROLE_MANAGER,
        ROLE_CUSTOMER
    }
}
