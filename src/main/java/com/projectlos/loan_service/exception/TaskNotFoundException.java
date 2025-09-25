package com.projectlos.loan_service.exception;

import org.springframework.http.HttpStatus;

public class TaskNotFoundException extends BaseException {
    
    public TaskNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND, "TASK_NOT_FOUND");
    }
    
    public TaskNotFoundException(String message, Throwable cause) {
        super(message, cause, HttpStatus.NOT_FOUND, "TASK_NOT_FOUND");
    }
}
