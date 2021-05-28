package com.stasroshchenko.diploma.annotation.validator;

import com.stasroshchenko.diploma.annotation.constraint.UniqueEmailConstraint;
import com.stasroshchenko.diploma.entity.database.ApplicationUser;
import com.stasroshchenko.diploma.service.application.user.ApplicationUserService;
import lombok.AllArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@AllArgsConstructor
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmailConstraint, String> {

    private final ApplicationUserService applicationUserService;

    @Override
    public void initialize(UniqueEmailConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        try {
            ApplicationUser user = applicationUserService
                    .loadUserByEmail(email);
        } catch (IllegalStateException ex) {
            return true;
        }

        return false;
    }

}