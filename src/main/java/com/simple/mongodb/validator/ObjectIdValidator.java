package com.simple.mongodb.validator;

import com.simple.mongodb.annotation.MongoId;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by Dong Wang.
 * Created on 15/2/14 14:25
 */
public class ObjectIdValidator implements ConstraintValidator<MongoId, String> {
    private boolean canNull;
    private boolean canEmpty;

    @Override
    public void initialize(MongoId constraintAnnotation) {
        canNull = constraintAnnotation.canNull();
        canEmpty = constraintAnnotation.canEmpty();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        boolean result;

        if (value == null) {
            result = canNull;
        } else {
            int valueLength = value.length();

            if (valueLength == 0) {
                result = canEmpty;
            } else {
                result = org.bson.types.ObjectId.isValid(value);
            }
        }
        return result;
    }
}
