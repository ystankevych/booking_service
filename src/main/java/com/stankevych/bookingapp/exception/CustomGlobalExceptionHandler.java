package com.stankevych.bookingapp.exception;

import com.stankevych.bookingapp.dto.exception.ApiErrorResponseDto;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomGlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ApiErrorResponseDto handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        return new ApiErrorResponseDto("Bad Request", getErrorMessages(e.getBindingResult()));
    }

    @ExceptionHandler(BookingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ApiErrorResponseDto handleBookingException(BookingException e) {
        return new ApiErrorResponseDto("Bad Request", e.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ApiErrorResponseDto handleEntityNotFoundException(EntityNotFoundException e) {
        return new ApiErrorResponseDto("Not Found", e.getMessage());
    }

    @ExceptionHandler(PaymentException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    protected ApiErrorResponseDto handlePaymentException(PaymentException e) {
        return new ApiErrorResponseDto("Payment Conflict", e.getMessage());
    }

    @ExceptionHandler(RegistrationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    protected ApiErrorResponseDto handleRegistrationException(RegistrationException e) {
        return new ApiErrorResponseDto("Registration Conflict", e.getMessage());
    }

    private List<String> getErrorMessages(BindingResult result) {
        return result.getAllErrors().stream()
                .map(ex -> ex instanceof FieldError ? ((FieldError) ex).getField()
                        + ex.getDefaultMessage() : ex.getDefaultMessage())
                .toList();
    }
}
