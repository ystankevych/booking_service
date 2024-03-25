package com.stankevych.booking_app.dto.user;

public record UpdateUserRequestDto(
        String email,
        String firstName,
        String lastName
) {}
