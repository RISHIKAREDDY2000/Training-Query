package com.training.query.Training.Query.repository;
import com.training.query.Training.Query.repository.EmployeeNotFoundException;


public class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException(String message) {
        super(message);
    }
}