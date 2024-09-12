package com.stankevych.booking_app.repository;

import com.stankevych.booking_app.model.TelegramUser;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TelegramUserRepository extends JpaRepository<TelegramUser, String> {
    @EntityGraph(attributePaths = "user")
    Optional<TelegramUser> findById(String id);

    @EntityGraph(attributePaths = "user")
    Optional<TelegramUser> findByUserIdAndIsActive(Long userId, boolean active);

    List<TelegramUser> findAllByIsActive(boolean active);
}
