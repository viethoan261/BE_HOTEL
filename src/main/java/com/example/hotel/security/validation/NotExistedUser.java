package com.example.hotel.security.validation;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = NotExistedUserValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface NotExistedUser {
    String message() default "User is existed.";
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

