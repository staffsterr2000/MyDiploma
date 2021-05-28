package com.stasroshchenko.diploma.annotation.constraint;

import com.stasroshchenko.diploma.annotation.validator.UniqueUsernameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueUsernameValidator.class)
@Target( { ElementType.FIELD, ElementType.METHOD } )
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueUsernameConstraint {

    String message() default "This username has already been taken";
    Class<?> [] groups() default {};
    Class<? extends Payload> [] payload() default {};

}
