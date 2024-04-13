package com.foxminded.payroll.exception;

public class ModelNotFoundException extends RuntimeException{
    public ModelNotFoundException(long id) {
        super("Model with id %d was not found".formatted(id));
    }

    public ModelNotFoundException(String name) {
        super("Model with name %s was not found".formatted(name));
    }
}
