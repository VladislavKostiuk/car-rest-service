package com.foxminded.payroll.exception;

public class UrlDoesNotMatchBodyException extends RuntimeException{
    public UrlDoesNotMatchBodyException() {
        super("The request body data doesn't match url");
    }
}
