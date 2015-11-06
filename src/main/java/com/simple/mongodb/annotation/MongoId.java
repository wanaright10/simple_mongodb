package com.simple.mongodb.annotation;


import com.simple.mongodb.validator.ObjectIdValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Created by Dong Wang.
 * Created on 15/2/14 14:23
 * <p>
 * must be objectId String
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {ObjectIdValidator.class})
public @interface MongoId {
    boolean canNull() default false;//可以为null，默认不可以

    boolean canEmpty() default false;//可以为""，默认不可以

    String message() default "mongodb String must be 24 char length!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}