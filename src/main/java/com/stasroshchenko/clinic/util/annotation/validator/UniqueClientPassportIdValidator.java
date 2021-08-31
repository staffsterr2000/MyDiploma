package com.stasroshchenko.clinic.util.annotation.validator;

import com.stasroshchenko.clinic.model.service.PersonDataService;
import com.stasroshchenko.clinic.util.annotation.constraint.UniqueClientPassportIdConstraint;
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
        return !personDataService.isAnyClientHasSuchPassportId(passportId);
    }

}
