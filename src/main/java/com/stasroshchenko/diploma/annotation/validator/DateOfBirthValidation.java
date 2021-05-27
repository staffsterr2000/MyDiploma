package com.stasroshchenko.diploma.annotation.validator;

import com.stasroshchenko.diploma.annotation.constraint.DateOfBirthConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.Month;

public class DateOfBirthValidation implements ConstraintValidator<DateOfBirthConstraint, LocalDate> {

    @Override
    public void initialize(DateOfBirthConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        return localDate.isAfter(LocalDate.of(1900, Month.JANUARY, 1)) &&
                localDate.isBefore(LocalDate.now().minusYears(18));
    }

}
