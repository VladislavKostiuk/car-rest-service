package com.foxminded.payroll.advice;

import com.foxminded.payroll.exception.SeveralCarsFoundException;
import com.foxminded.payroll.exception.SeveralModelsFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class SeveralEntitiesFoundAdvice {
    @ResponseBody
    @ExceptionHandler(SeveralModelsFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String severalModelsFound(SeveralModelsFoundException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(SeveralCarsFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String severalCarsFound(SeveralCarsFoundException ex) {
        return ex.getMessage();
    }
}
