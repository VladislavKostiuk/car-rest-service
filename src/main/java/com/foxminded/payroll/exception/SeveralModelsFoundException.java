package com.foxminded.payroll.exception;

public class SeveralModelsFoundException extends RuntimeException{
    public SeveralModelsFoundException() {
        super("Two or more models were found with the same name");
    }
}
