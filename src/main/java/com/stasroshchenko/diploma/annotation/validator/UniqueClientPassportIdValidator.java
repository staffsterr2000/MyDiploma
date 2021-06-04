package com.stasroshchenko.diploma.annotation.validator;

import com.stasroshchenko.diploma.annotation.constraint.UniqueClientPassportIdConstraint;
import com.stasroshchenko.diploma.service.PersonDataService;
import lombok.AllArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@AllArgsConstructor
public class UniqueClientPassportIdValidator implements ConstraintValidator<UniqueClientPassportIdConstraint, Long> {

    private final PersonDataService personDataService;

    @Override
    public void initialize(UniqueClientPassportIdConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(Long passportId, ConstraintValidatorContext constraintValidatorContext) {
        return !personDataService.isClientTableHasPasswordId(passportId);
    }

}
