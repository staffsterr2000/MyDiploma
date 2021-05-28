package com.stasroshchenko.diploma.annotation.constraint;

import com.stasroshchenko.diploma.annotation.validator.UniqueEmailValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueEmailValidator.class)
@Target( { ElementType.FIELD, ElementType.METHOD } )
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueEmailConstraint {

    String message() default "This email has already been taken";
    Class<?> [] groups() default {};
    Class<? extends Payload> [] payload() default {};

}
