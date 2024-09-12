package com.stankevych.bookingapp.dto.user;

import java.util.Set;

public record UserWithRolesResponseDto(
        String email,
        Set<Long> rolesId) {
}
