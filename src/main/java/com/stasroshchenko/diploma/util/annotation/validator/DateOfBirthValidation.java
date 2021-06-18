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
        return dateOfBirth      // щоб бути валідною, дата народження повинна:
                // 1. бути після 01.01.1900
                .isAfter(LocalDate.of(1900, Month.JANUARY, 1)) &&
                // 2. бути раніше ніж (зараз - 18 років) (тобто людина повинна бути повнолітня)
                dateOfBirth.isBefore(LocalDate.now().minusYears(18));
    }

}
