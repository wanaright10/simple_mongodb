package com.simple.mongodb.validator;


import com.simple.mongodb.annotation.NotHtml;
import com.simple.mongodb.util.EmptyUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by Dong Wang.
 * Created on 14/12/29 下午1:37
 */
public class NotHtmlValidator implements ConstraintValidator<NotHtml, String> {

    @Override
    public void initialize(NotHtml parameters) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return !EmptyUtil.isNullOrEmpty(value) &&
                !value.contains(">") &&
                !value.contains("<");
    }
}
