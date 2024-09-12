package com.stankevych.booking_app.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "telegram_users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TelegramUser {
    @Id
    private String id;

    @OneToOne
    @JoinColumn(name = "user_id",
    foreignKey = @ForeignKey(name = "telegram_users_fk"))
    private User user;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Column
    private String validationCode;

    public TelegramUser(String id) {
        this.id = id;
    }
}
