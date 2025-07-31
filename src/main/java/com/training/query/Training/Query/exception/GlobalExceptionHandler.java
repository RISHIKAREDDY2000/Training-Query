package com.training.query.Training.Query.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntime(RuntimeException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Runtime Exception");
        error.put("message", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneric(Exception ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Exception");
        error.put("message", ex.getMessage());
        return ResponseEntity.internalServerError().body(error);
    }
    @ExceptionHandler(SchedulerNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleSchedulerNotFound(SchedulerNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("errorCode", "SCHEDULER_NOT_FOUND");
        error.put("message", ex.getMessage());
        error.put("cause", ex.getClass().getSimpleName());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}


