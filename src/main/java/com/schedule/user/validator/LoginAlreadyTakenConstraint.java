package com.schedule.user.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = LoginAlreadyTakenValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginAlreadyTakenConstraint {
    String message() default "Login is already taken";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
