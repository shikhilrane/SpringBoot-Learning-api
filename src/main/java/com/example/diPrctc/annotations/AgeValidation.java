package com.example.diPrctc.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(
        validatedBy = {AgeValidator.class}
)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
public @interface AgeValidation {
    String message() default "Age of the employee must be between 18 to 80";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
