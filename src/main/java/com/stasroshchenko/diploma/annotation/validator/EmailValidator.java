package com.stasroshchenko.diploma.annotation.validator;

import com.stasroshchenko.diploma.annotation.constraint.EmailConstraint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

@Service
public class EmailValidator implements ConstraintValidator<EmailConstraint, String> {

    private final static Logger LOGGER =
            LoggerFactory.getLogger(EmailValidator.class);

    @Override
    public void initialize(EmailConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        try {
            return Pattern.matches(
                    "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&’*+/=?`{|}~^-]+)*" +
                            "@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)+$", email
            );
        } catch (NullPointerException ex) {
            LOGGER.error("nullPointer", ex);
            throw ex;
        }
    }

}
