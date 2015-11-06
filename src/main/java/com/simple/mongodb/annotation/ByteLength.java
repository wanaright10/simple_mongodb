package com.simple.mongodb.annotation;


import com.simple.mongodb.validator.ByteLengthValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Created by Dong Wang.
 * Created on 2014/9/17 下午 12:16.
 * <p/>
 * validate String value's UTF-8 byte array length
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {ByteLengthValidator.class})
public @interface ByteLength {
    int min() default -1;//if min is -1 the target can be null

    int max() default Integer.MAX_VALUE;

    String message() default "{cn.maihahacs.base.ByteLength.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
