package com.foxminded.payroll.advice;

import com.foxminded.payroll.exception.UrlDoesNotMatchBodyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class UrlDoesNotMatchBodyAdvice {
    @ResponseBody
    @ExceptionHandler(UrlDoesNotMatchBodyException.class)
    ResponseEntity<ProblemDetail> urlDoesNotMatchUrl(UrlDoesNotMatchBodyException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        return ResponseEntity.badRequest().body(problemDetail);
    }
}
