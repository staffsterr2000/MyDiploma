package com.stasroshchenko.diploma.annotation.validator;

import com.stasroshchenko.diploma.annotation.constraint.UniqueUsernameConstraint;
import com.stasroshchenko.diploma.entity.database.ApplicationUser;
import com.stasroshchenko.diploma.service.user.ApplicationUserService;
import lombok.AllArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@AllArgsConstructor
public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsernameConstraint, String> {

    private final ApplicationUserService applicationUserService;

    @Override
    public void initialize(UniqueUsernameConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        try {
            ApplicationUser user = applicationUserService
                    .getByUsername(username);
        } catch (IllegalStateException ex) {
            return true;
        }

        return false;
    }

}
