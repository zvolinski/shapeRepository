package com.example.exerciese.config;

import com.example.exerciese.exception.exception.ShapeInvalidIdException;
import com.example.exerciese.exception.exception.ShapeInvalidPerimetersException;
import com.example.exerciese.exception.exception.ShapeInvalidTypeException;
import com.example.exerciese.exception.exception.ShapeNotFoundByIdException;
import com.example.exerciese.exception.exception.ShapeNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //ShapeNotFoundException
    @ExceptionHandler(ShapeNotFoundException.class)
    public ResponseEntity<String> shapeNotFoundException(ShapeNotFoundByIdException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Do not found entity for specific type: " + e.getMessage());
    }

    //Validation for Invalid Type
    @ExceptionHandler(ShapeInvalidTypeException.class)
    public ResponseEntity<String> invalidTypeException(ShapeInvalidTypeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Type: " + e.getMessage());
    }

    //Validation for Invalid Perimeters
    @ExceptionHandler(ShapeInvalidPerimetersException.class)
    public ResponseEntity<String> invalidPerimetersException(ShapeInvalidPerimetersException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Perimeters: " + e.getMessage());
    }

    //Validation for ID NOT_FOUND
    @ExceptionHandler(ShapeNotFoundByIdException.class)
    public ResponseEntity<String> validateIdException(ShapeNotFoundByIdException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Do not found entity with id: " + e.getMessage());
    }

    //Validation for ID BAD_REQUEST - format
    @ExceptionHandler(ShapeInvalidIdException.class)
    public ResponseEntity<String> validateFormatForID(ShapeInvalidIdException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid ID format: " + e.getMessage());
    }
}

