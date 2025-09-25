package com.projectlos.loan_service.exception;

import org.springframework.http.HttpStatus;

public class LoanApplicationNotFoundException extends BaseException {
    
    public LoanApplicationNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND, "LOAN_APPLICATION_NOT_FOUND");
    }
    
    public LoanApplicationNotFoundException(String message, Throwable cause) {
        super(message, cause, HttpStatus.NOT_FOUND, "LOAN_APPLICATION_NOT_FOUND");
    }
}
