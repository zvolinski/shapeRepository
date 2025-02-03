package com.example.exerciese;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //Obsługa wyjątków niepoprawnych wprowadzenia danych
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("IllegalArgumentException" + ex.getMessage());
    }

    // Obsługa wyjątków EntityNotFoundException
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException exceptionNotFound) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Entity do not found" + exceptionNotFound.getMessage());
    }

    // Obsługa wszystkich innych wyjątków
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception exceptionGeneral) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred: " + exceptionGeneral.getMessage());
    }

    //Obsługa wyjątków walidacji
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleValidationException(ConstraintViolationException exceptionNotValid) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Validation failed: " + exceptionNotValid.getMessage());
    }

}

