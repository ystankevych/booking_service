package com.stankevych.booking_app.dto.user;

import java.util.Set;

public record UserWithRolesResponseDto(
        String email,
        Set<Long> rolesId) {
}
