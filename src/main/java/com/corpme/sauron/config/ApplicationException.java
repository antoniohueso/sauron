package com.corpme.sauron.config;

import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;

public class ApplicationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    List<String> errors = new ArrayList<String>();

    public ApplicationException(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        addMessageToErrors(message);
    }

    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
        addMessageToErrors(message);
    }

    public ApplicationException(String message) {
        super(message);
        addMessageToErrors(message);
    }

    public ApplicationException(List<ObjectError> errs) {
        for (ObjectError err : errs) {
            errors.add(err.getDefaultMessage());
        }
    }

    public List<String> getErrors() {
        return errors;
    }

    void addMessageToErrors(String message) {
        errors.add(message);
    }
}
