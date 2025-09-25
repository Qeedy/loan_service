package com.projectlos.loan_service.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends BaseException {
    
    public UnauthorizedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED, "UNAUTHORIZED");
    }
    
    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause, HttpStatus.UNAUTHORIZED, "UNAUTHORIZED");
    }
}
