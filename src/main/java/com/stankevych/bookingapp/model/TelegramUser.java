package com.stankevych.bookingapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
