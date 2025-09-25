package com.projectlos.loan_service.exception;

import org.springframework.http.HttpStatus;

public class InvalidLoanApplicationException extends BaseException {
    
    public InvalidLoanApplicationException(String message) {
        super(message, HttpStatus.BAD_REQUEST, "INVALID_LOAN_APPLICATION");
    }
    
    public InvalidLoanApplicationException(String message, Throwable cause) {
        super(message, cause, HttpStatus.BAD_REQUEST, "INVALID_LOAN_APPLICATION");
    }
}
