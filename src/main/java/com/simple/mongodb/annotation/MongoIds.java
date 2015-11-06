package com.simple.mongodb.annotation;


import com.simple.mongodb.validator.ObjectIdsValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Created by Dong Wang.
 * Created on 15/4/10 14:29
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {ObjectIdsValidator.class})
public @interface MongoIds {
    boolean canNull() default false;

    boolean canEmpty() default false;

    String message() default "mongodb String must be 24 char length!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
