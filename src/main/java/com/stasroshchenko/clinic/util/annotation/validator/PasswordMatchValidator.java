package com.stasroshchenko.clinic.util.annotation.validator;

import com.stasroshchenko.clinic.util.annotation.constraint.PasswordMatchConstraint;
import com.stasroshchenko.clinic.request.RegistrationRequest;

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
