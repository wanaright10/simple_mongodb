package com.simple.mongodb.validator;

import com.simple.mongodb.annotation.MongoIds;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

/**
 * Created by Dong Wang.
 * Created on 15/4/10 14:29
 */
public class ObjectIdsValidator implements ConstraintValidator<MongoIds, List<String>> {
    private boolean canNull;
    private boolean canEmpty;

    @Override
    public void initialize(MongoIds constraintAnnotation) {
        canNull = constraintAnnotation.canNull();
        canEmpty = constraintAnnotation.canEmpty();
    }

    @Override
    public boolean isValid(List<String> value, ConstraintValidatorContext context) {
        boolean result;

        if (value == null) {
            result = canNull;
        } else {
            int valueLength = value.size();

            if (valueLength == 0) {
                result = canEmpty;
            } else {
                result = true;
                for (String mongoId : value) {
                    if (!org.bson.types.ObjectId.isValid(mongoId)) {
                        result = false;
                        break;
                    }
                }
            }
        }
        return result;
    }
}