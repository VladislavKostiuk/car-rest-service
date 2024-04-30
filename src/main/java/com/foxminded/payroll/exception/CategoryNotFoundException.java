package com.foxminded.payroll.exception;

public class CategoryNotFoundException extends RuntimeException{
    public CategoryNotFoundException(long id) {
        super("Category with id %d was not found ".formatted(id));
    }

    public CategoryNotFoundException(String name) {
        super("Category with name %s was not found ".formatted(name));
    }
}
