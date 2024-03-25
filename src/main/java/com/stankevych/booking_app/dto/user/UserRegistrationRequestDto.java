package com.stankevych.booking_app.dto.user;

public record UserRegistrationRequestDto(
        String email,
        String password,
        String repeatPassword,
        String firstName,
        String lastName
) {}
