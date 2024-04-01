package com.stankevych.booking_app.dto.user;

import com.stankevych.booking_app.validation.email.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateUserRequestDto(
        @Email
        String email,

        @NotBlank(message = "First name must not be null or empty")
        String firstName,

        @NotBlank(message = "Last name must not be null or empty")
        String lastName) {
}
