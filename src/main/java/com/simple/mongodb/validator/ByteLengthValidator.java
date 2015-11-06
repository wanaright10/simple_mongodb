package com.simple.mongodb.validator;

import com.simple.mongodb.annotation.ByteLength;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by Dong Wang.
 * Created on 2014/9/17 下午 12:18.
 */
public class ByteLengthValidator implements ConstraintValidator<ByteLength, String> {
    private static final Log log = LoggerFactory.make();

    private int min;
    private int max;

    @Override
    public void initialize(ByteLength parameters) {
        min = parameters.min();
        max = parameters.max();

        validateParameters();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return min == -1;//if min is -1 the target can be null
        }

        int length;
        try {
            length = value.getBytes("UTF-8").length;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
        return length >= min && length <= max;
    }

    private void validateParameters() {
        if (min < -1) {
            throw log.getMinCannotBeNegativeException();
        }
        if (max < 0) {
            throw log.getMaxCannotBeNegativeException();
        }
        if (max < min) {
            throw log.getLengthCannotBeNegativeException();
        }
    }
}
