package com.simple.mongodb.annotation;


import com.simple.mongodb.validator.NotHtmlValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Created by Dong Wang.
 * Created on 2014/10/8 下午 21:24.
 * <p>
 * 会过滤掉英文的大小于符号
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {NotHtmlValidator.class})
public @interface NotHtml {
    String message() default "{cn.maihahacs.base.NotHtml.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
