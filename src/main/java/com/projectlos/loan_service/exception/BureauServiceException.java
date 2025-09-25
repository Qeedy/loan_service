package com.projectlos.loan_service.exception;

import org.springframework.http.HttpStatus;

public class BureauServiceException extends BaseException {
    
    public BureauServiceException(String message) {
        super(message, HttpStatus.SERVICE_UNAVAILABLE, "BUREAU_SERVICE_ERROR");
    }
    
    public BureauServiceException(String message, Throwable cause) {
        super(message, cause, HttpStatus.SERVICE_UNAVAILABLE, "BUREAU_SERVICE_ERROR");
    }
}
