package com.stasroshchenko.diploma.annotation.validator;

import com.stasroshchenko.diploma.annotation.constraint.PasswordMatchConstraint;
import com.stasroshchenko.diploma.entity.request.RegistrationRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchValidator implements
        ConstraintValidator<PasswordMatchConstraint, RegistrationRequest> {

    @Override
    public void initialize(PasswordMatchConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(RegistrationRequest request, ConstraintValidatorContext constraintValidatorContext) {
        String password = request.getPassword();
        String repeatPassword = request.getRepeatedPassword();

        return password != null && password.equals(repeatPassword);
    }
}
