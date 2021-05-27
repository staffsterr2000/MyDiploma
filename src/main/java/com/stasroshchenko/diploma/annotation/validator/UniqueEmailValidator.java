package com.stasroshchenko.diploma.annotation.validator;

import com.stasroshchenko.diploma.annotation.constraint.UniqueEmailConstraint;
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
public class UniqueEmailValidator implements
        ConstraintValidator<UniqueEmailConstraint, String> {

    private final ApplicationUserService applicationUserService;
    private final static Logger LOGGER =
            LoggerFactory.getLogger(UniqueEmailValidator.class);

    @Override
    public void initialize(UniqueEmailConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        try {
            applicationUserService.loadUserByOnlyEmail(email);

        } catch (IllegalStateException ex) {
            LOGGER.error(ex.getMessage());
            return true;
        }

        return false;
    }

}
