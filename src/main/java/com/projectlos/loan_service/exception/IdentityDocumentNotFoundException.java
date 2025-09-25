package com.projectlos.loan_service.exception;

import org.springframework.http.HttpStatus;

public class IdentityDocumentNotFoundException extends BaseException {
    
    public IdentityDocumentNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND, "IDENTITY_DOCUMENT_NOT_FOUND");
    }
    
    public IdentityDocumentNotFoundException(String message, Throwable cause) {
        super(message, cause, HttpStatus.NOT_FOUND, "IDENTITY_DOCUMENT_NOT_FOUND");
    }
}
