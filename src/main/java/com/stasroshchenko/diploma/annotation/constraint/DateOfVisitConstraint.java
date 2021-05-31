package com.stasroshchenko.diploma.annotation.constraint;

import com.stasroshchenko.diploma.annotation.validator.DateOfVisitValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DateOfVisitValidation.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface DateOfVisitConstraint {

    String message() default "Date is invalid. Try picking up a more realistic one.";
    Class<?> [] groups() default {};
    Class<? extends Payload> [] payload() default {};

}