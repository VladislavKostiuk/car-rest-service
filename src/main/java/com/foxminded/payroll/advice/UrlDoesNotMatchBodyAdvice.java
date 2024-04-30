package com.foxminded.payroll.advice;

import com.foxminded.payroll.exception.SeveralCarsFoundException;
import com.foxminded.payroll.exception.UrlDoesNotMatchBodyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class UrlDoesNotMatchBodyAdvice {
    @ResponseBody
    @ExceptionHandler(UrlDoesNotMatchBodyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String urlDoesNotMatchUrl(UrlDoesNotMatchBodyException ex) {
        return ex.getMessage();
    }
}
