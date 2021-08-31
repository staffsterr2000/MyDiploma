package com.stasroshchenko.clinic.util.annotation.constraint;

import com.stasroshchenko.clinic.util.annotation.validator.PasswordMatchValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Validates do passwords match each other.
 * @author staffsterr2000
 * @version 1.0
 */
@Documented
@Constraint(validatedBy = PasswordMatchValidator.class)
@Target( { ElementType.TYPE } )
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordMatchConstraint {

    String message() default "Please repeat your password correctly";
    Class<?> [] groups() default {};
    Class<? extends Payload> [] payload() default {};

}
