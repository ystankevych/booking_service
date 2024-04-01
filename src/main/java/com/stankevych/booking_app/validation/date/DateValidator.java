package com.stankevych.booking_app.validation.date;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

import java.time.LocalDate;

public class DateValidator implements ConstraintValidator<Date, LocalDate> {
    private String fromDate;
    private String toDate;
    @Override
    public void initialize(Date constraintAnnotation) {
        this.fromDate = constraintAnnotation.fields()[0];
        this.toDate = constraintAnnotation.fields()[1];
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        var from = new BeanWrapperImpl(value).getPropertyValue(fromDate);
        var to = new BeanWrapperImpl(value).getPropertyValue(toDate);
        return from != null && to != null
                && ((LocalDate)from).isBefore(((LocalDate)to));
    }
}
