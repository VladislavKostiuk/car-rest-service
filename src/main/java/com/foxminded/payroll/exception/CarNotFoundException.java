package com.foxminded.payroll.exception;

public class CarNotFoundException extends RuntimeException{
    public CarNotFoundException(long id) {
        super("Car with id %d was not found ".formatted(id));
    }

    public CarNotFoundException() {
        super("Car with this parameters was not found ");
    }
}
