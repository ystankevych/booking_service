package com.stankevych.bookingapp.dto.user;

import com.stankevych.bookingapp.validation.email.Email;
import com.stankevych.bookingapp.validation.password.FieldMatch;
import jakarta.validation.constraints.NotBlank;

@FieldMatch(fields = {"password", "repeatPassword"},
        message = "Password and repeatPassword are not equal")
public record UserRegistrationRequestDto(
        @Email
        String email,

        @NotBlank(message = "Password must not be null or empty")
        String password,

        @NotBlank(message = "Repeat password must not be null or empty")
        String repeatPassword,

        @NotBlank(message = "First name must not be null or empty")
        String firstName,

        @NotBlank(message = "Last name must not be null or empty")
        String lastName) {
}
