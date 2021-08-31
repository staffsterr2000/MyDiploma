package com.stasroshchenko.clinic.util.annotation.validator;

import com.stasroshchenko.clinic.entity.user.ApplicationUser;
import com.stasroshchenko.clinic.model.service.user.ApplicationUserService;
import com.stasroshchenko.clinic.util.annotation.constraint.UniqueEmailConstraint;
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
                    .getByEmail(email);
        } catch (IllegalStateException ex) {
            return true;
        }

        return false;
    }

}