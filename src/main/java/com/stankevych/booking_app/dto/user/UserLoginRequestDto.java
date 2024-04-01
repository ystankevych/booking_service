package com.stankevych.booking_app.dto.user;

import com.stankevych.booking_app.validation.email.Email;
import jakarta.validation.constraints.NotBlank;

public record UserLoginRequestDto(
        @Email
        String email,

        @NotBlank(message = "Password must not be null or empty")
        String password) {
}
