package com.stasroshchenko.diploma.annotation.validator;

import com.stasroshchenko.diploma.annotation.constraint.UniqueUsernameConstraint;
import com.stasroshchenko.diploma.service.ApplicationUserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Service
@AllArgsConstructor
@Deprecated
public class UniqueUsernameValidator implements
        ConstraintValidator<UniqueUsernameConstraint, String> {

    private final ApplicationUserService applicationUserService;
    private final static Logger LOGGER =
            LoggerFactory.getLogger(UniqueUsernameValidator.class);

    @Override
    public void initialize(UniqueUsernameConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        try {
            applicationUserService.loadUserByOnlyUsername(username);

        } catch (IllegalStateException ex) {
            LOGGER.error(ex.getMessage());
            return true;
        }

        return false;
    }

}
