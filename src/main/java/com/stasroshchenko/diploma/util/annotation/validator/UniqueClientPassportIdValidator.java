package com.stasroshchenko.diploma.util.annotation.validator;

import com.stasroshchenko.diploma.util.annotation.constraint.UniqueClientPassportIdConstraint;
import com.stasroshchenko.diploma.model.service.PersonDataService;
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
