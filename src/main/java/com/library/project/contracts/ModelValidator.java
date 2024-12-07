package com.library.project.contracts;

import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

public abstract class ModelValidator {

    /**
     * @param <T>
     * @param model
     * @return
     * @throws ConstraintViolationException
     */
    public static <T> T validate(T model) throws ConstraintViolationException {
        
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        Set<ConstraintViolation<T>> violations = validator.validate(model);

        if (violations.isEmpty()) {
            return model;
        }

        throw new ConstraintViolationException(violations);
    }
}
