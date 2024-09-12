package com.stankevych.bookingapp.validation.date;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import org.springframework.beans.BeanWrapperImpl;

public class DateValidator implements ConstraintValidator<Date, Object> {
    private String fromDate;
    private String toDate;

    @Override
    public void initialize(Date constraintAnnotation) {
        this.fromDate = constraintAnnotation.fields()[0];
        this.toDate = constraintAnnotation.fields()[1];
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        var from = new BeanWrapperImpl(value).getPropertyValue(fromDate);
        var to = new BeanWrapperImpl(value).getPropertyValue(toDate);
        return from != null && to != null
                && ((LocalDate)from).isBefore(((LocalDate)to));
    }
}
