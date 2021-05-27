package com.stasroshchenko.diploma.constraint;

import com.stasroshchenko.diploma.constraint.validator.UsernameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UsernameValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD } )
@Retention(RetentionPolicy.RUNTIME)
public @interface UsernameConstraint {

    String message() default "Invalid username. Try using no symbols except(_)";
    Class<?> [] groups() default {};
    Class<? extends Payload> [] payload() default {};

}
