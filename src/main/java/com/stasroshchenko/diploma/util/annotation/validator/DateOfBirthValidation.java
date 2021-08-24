package com.stasroshchenko.diploma.util.annotation.validator;

import com.stasroshchenko.diploma.util.annotation.constraint.DateOfBirthConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.Month;

public class DateOfBirthValidation implements ConstraintValidator<DateOfBirthConstraint, LocalDate> {

    @Override
    public void initialize(DateOfBirthConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(LocalDate dateOfBirth, ConstraintValidatorContext constraintValidatorContext) {
        return dateOfBirth
                // the DoB must be after 01.01.1900
                .isAfter(LocalDate.of(1900, Month.JANUARY, 1)) &&
                // the person must be adult
                dateOfBirth.isBefore(LocalDate.now().minusYears(18));
    }

}
