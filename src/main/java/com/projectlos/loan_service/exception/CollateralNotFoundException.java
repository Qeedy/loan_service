package com.projectlos.loan_service.exception;

import org.springframework.http.HttpStatus;

public class CollateralNotFoundException extends BaseException {
    
    public CollateralNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND, "COLLATERAL_NOT_FOUND");
    }
    
    public CollateralNotFoundException(String message, Throwable cause) {
        super(message, cause, HttpStatus.NOT_FOUND, "COLLATERAL_NOT_FOUND");
    }
}
