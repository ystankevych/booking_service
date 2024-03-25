package com.stankevych.booking_app.dto.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

import java.util.Set;

public record UpdateUserByRoleIdDto(
        @NotEmpty(message = "Roles list can't be null or empty")
        Set<@Positive Long> rolesId
) {}
