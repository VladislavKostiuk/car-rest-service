package com.foxminded.payroll.advice;

import com.foxminded.payroll.exception.CarNotFoundException;
import com.foxminded.payroll.exception.CategoryNotFoundException;
import com.foxminded.payroll.exception.ManufacturerNotFoundException;
import com.foxminded.payroll.exception.ModelNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class EntityNotFoundAdvice {
    @ResponseBody
    @ExceptionHandler(CarNotFoundException.class)
    ResponseEntity<ProblemDetail> carNotFoundHandler(CarNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(problemDetail, HttpStatus.NOT_FOUND);
    }

    @ResponseBody
    @ExceptionHandler(CategoryNotFoundException.class)
    ResponseEntity<ProblemDetail> categoryNotFoundHandler(CategoryNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(problemDetail, HttpStatus.NOT_FOUND);
    }

    @ResponseBody
    @ExceptionHandler(ManufacturerNotFoundException.class)
    ResponseEntity<ProblemDetail> manufacturerNotFoundHandler(ManufacturerNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(problemDetail, HttpStatus.NOT_FOUND);
    }

    @ResponseBody
    @ExceptionHandler(ModelNotFoundException.class)
    ResponseEntity<ProblemDetail> modelNotFoundHandler(ModelNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(problemDetail, HttpStatus.NOT_FOUND);
    }
}
