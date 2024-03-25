package com.stankevych.booking_app.dto.user;

public record UserResponseDto(
        Long id,
        String email,
        String firstName,
        String lastName
) {}
