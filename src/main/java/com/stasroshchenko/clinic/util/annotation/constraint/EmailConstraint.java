package com.stasroshchenko.clinic.util.annotation.constraint;

import com.stasroshchenko.clinic.util.annotation.validator.EmailValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Validates an email's format.
 * @author staffsterr2000
 * @version 1.0
 */
@Documented
@Constraint(validatedBy = EmailValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD } )
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailConstraint {

    String message() default "Invalid email";
    Class<?> [] groups() default {};
    Class<? extends Payload> [] payload() default {};

}
