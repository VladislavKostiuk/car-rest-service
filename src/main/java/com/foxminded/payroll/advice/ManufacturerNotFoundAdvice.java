package com.foxminded.payroll.advice;

import com.foxminded.payroll.exception.ManufacturerNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ManufacturerNotFoundAdvice {
    @ResponseBody
    @ExceptionHandler(ManufacturerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String manufacturerNotFoundHandler(ManufacturerNotFoundException ex) {
        return ex.getMessage();
    }
}
