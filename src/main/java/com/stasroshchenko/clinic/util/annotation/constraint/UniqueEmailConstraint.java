package com.stasroshchenko.clinic.util.annotation.constraint;

import com.stasroshchenko.clinic.util.annotation.validator.UniqueEmailValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Validates that the email doesn't exist in the DB.
 * @author staffsterr2000
 * @version 1.0
 */
@Documented
@Constraint(validatedBy = UniqueEmailValidator.class)
@Target( { ElementType.FIELD, ElementType.METHOD } )
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueEmailConstraint {

    String message() default "This email has already been taken";
    Class<?> [] groups() default {};
    Class<? extends Payload> [] payload() default {};

}
