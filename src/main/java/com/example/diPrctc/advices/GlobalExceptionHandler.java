package com.example.diPrctc.advices;

import com.example.diPrctc.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFound(ResourceNotFoundException exception){
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.NOT_FOUND)
                .message(exception.getMessage().toUpperCase())
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleInternalServerError(Exception exception){
        ApiError apiError = ApiError.builder()                          // Creates an ApiError object using Lombok's builder pattern with NOT_FOUND status and message
                .status(HttpStatus.INTERNAL_SERVER_ERROR)               // Sets HTTP status to 404
                .message(exception.getMessage())                        // Gets a custom error message
                .build();                                               // Builds the ApiError object
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);    // Returns the ApiError wrapped in a ResponseEntity with 404 status
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleInputValidationErrors(MethodArgumentNotValidException e) {
        List<String> errors = e
                .getBindingResult()
                .getAllErrors()
                .stream()
                .map(objectError -> objectError.getDefaultMessage())
                .collect(Collectors.toList());

        ApiError apiError = ApiError.builder()                          // Creates an ApiError object using Lombok's builder pattern with NOT_FOUND status and message
                .status(HttpStatus.BAD_REQUEST)                         // Sets HTTP status to 404
                .message("Input validation failed")                     // Gets a custom error message
                .subErrors(errors)
                .build();                                               // Builds the ApiError object
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }
}
