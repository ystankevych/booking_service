package com.stankevych.bookingapp.repository;

import com.stankevych.bookingapp.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
