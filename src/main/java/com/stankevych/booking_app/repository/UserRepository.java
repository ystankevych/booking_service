package com.stankevych.booking_app.repository;

import com.stankevych.booking_app.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = "roles")
    Optional<User> findByEmail(String email);

    @EntityGraph(attributePaths = "roles")
    Optional<User> findById(Long id);

    boolean existsByEmail(String email);
}
