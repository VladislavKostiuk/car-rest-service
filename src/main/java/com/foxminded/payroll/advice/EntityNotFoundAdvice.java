package com.foxminded.payroll.advice;

import com.foxminded.payroll.exception.CarNotFoundException;
import com.foxminded.payroll.exception.CategoryNotFoundException;
import com.foxminded.payroll.exception.ManufacturerNotFoundException;
import com.foxminded.payroll.exception.ModelNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class EntityNotFoundAdvice {
    @ResponseBody
    @ExceptionHandler(CarNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String carNotFoundHandler(CarNotFoundException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(CategoryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String categoryNotFoundHandler(CategoryNotFoundException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(ManufacturerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String manufacturerNotFoundHandler(ManufacturerNotFoundException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(ModelNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String modelNotFoundHandler(ModelNotFoundException ex) {
        return ex.getMessage();
    }
}
