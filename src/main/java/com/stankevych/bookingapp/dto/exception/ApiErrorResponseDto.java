package com.stankevych.bookingapp.dto.exception;

public record ApiErrorResponseDto(
        String status,
        Object errorMessage) {
}
