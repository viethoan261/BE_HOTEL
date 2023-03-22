package com.example.hotel.validation.annotation;

import com.example.hotel.validation.validator.UniqueRoomNameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniqueRoomNameValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface UniqueRoomName {
    String message() default "The room name already used.";
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
