package com.projectlos.loan_service.exception;

import org.springframework.http.HttpStatus;

public class LoanApplicationConflictException extends BaseException {
    
    public LoanApplicationConflictException(String message) {
        super(message, HttpStatus.CONFLICT, "LOAN_APPLICATION_CONFLICT");
    }
    
    public LoanApplicationConflictException(String message, Throwable cause) {
        super(message, cause, HttpStatus.CONFLICT, "LOAN_APPLICATION_CONFLICT");
    }
}
