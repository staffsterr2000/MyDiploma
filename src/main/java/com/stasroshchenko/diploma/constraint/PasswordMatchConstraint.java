package com.stasroshchenko.diploma.constraint;

import com.stasroshchenko.diploma.constraint.validator.PasswordMatchValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordMatchValidator.class)
@Target( { ElementType.TYPE } )
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordMatchConstraint {

    String message() default "Please repeat your password correctly";
    Class<?> [] groups() default {};
    Class<? extends Payload> [] payload() default {};

}
