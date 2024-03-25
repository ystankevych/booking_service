package com.stankevych.booking_app.repository;

import com.stankevych.booking_app.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
