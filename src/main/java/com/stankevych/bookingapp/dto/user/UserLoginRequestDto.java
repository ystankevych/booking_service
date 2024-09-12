package com.stankevych.bookingapp.dto.user;

import com.stankevych.bookingapp.validation.email.Email;
import jakarta.validation.constraints.NotBlank;

public record UserLoginRequestDto(
        @Email
        String email,

        @NotBlank(message = "Password must not be null or empty")
        String password) {
}
