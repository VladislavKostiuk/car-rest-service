package com.foxminded.payroll.exception;

public class SeveralCarsFoundException extends RuntimeException{
    public SeveralCarsFoundException() {
        super("Two or more cars were found with the same model and year");
    }
}
