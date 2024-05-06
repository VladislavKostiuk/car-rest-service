package com.foxminded.payroll.advice;

import com.foxminded.payroll.exception.SeveralCarsFoundException;
import com.foxminded.payroll.exception.SeveralModelsFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class SeveralEntitiesFoundAdvice {
    @ResponseBody
    @ExceptionHandler(SeveralModelsFoundException.class)
    ResponseEntity<ProblemDetail> severalModelsFound(SeveralModelsFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        return ResponseEntity.badRequest().body(problemDetail);
    }

    @ResponseBody
    @ExceptionHandler(SeveralCarsFoundException.class)
    ResponseEntity<ProblemDetail> severalCarsFound(SeveralCarsFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        return ResponseEntity.badRequest().body(problemDetail);
    }
}
