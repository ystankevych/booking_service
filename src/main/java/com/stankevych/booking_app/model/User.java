package com.stankevych.booking_app.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Getter
@ToString
@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String password;

    @ManyToMany
    @JoinTable(name = "users_roles",
    joinColumns = @JoinColumn(name = "user_id",
    foreignKey = @ForeignKey(name = "users_roles_users_fk")),
    inverseJoinColumns = @JoinColumn(name = "role_id",
    foreignKey = @ForeignKey(name = "users_roles_roles_fk")))
    private Set<Role> roles = new HashSet<>();
}
