package com.stasroshchenko.diploma.util.annotation.constraint;

import com.stasroshchenko.diploma.util.annotation.validator.UniqueUsernameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


/**
 * Validates that the username doesn't exist in the DB.
 * @author staffsterr2000
 * @version 1.0
 */
@Documented
@Constraint(validatedBy = UniqueUsernameValidator.class)
@Target( { ElementType.FIELD, ElementType.METHOD } )
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueUsernameConstraint {

    String message() default "This username has already been taken";
    Class<?> [] groups() default {};
    Class<? extends Payload> [] payload() default {};

}
