package com.foxminded.payroll.exception;

public class ManufacturerNotFoundException extends RuntimeException{
    public ManufacturerNotFoundException(long id) {
        super("Manufacturer with id %d was not found".formatted(id));
    }

    public ManufacturerNotFoundException(String name) {
        super("Manufacturer with name %s was not found".formatted(name));
    }
}
