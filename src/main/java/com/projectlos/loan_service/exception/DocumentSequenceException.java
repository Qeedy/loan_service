package com.projectlos.loan_service.exception;

import org.springframework.http.HttpStatus;

public class DocumentSequenceException extends BaseException {
    
    public DocumentSequenceException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR, "DOCUMENT_SEQUENCE_ERROR");
    }
    
    public DocumentSequenceException(String message, Throwable cause) {
        super(message, cause, HttpStatus.INTERNAL_SERVER_ERROR, "DOCUMENT_SEQUENCE_ERROR");
    }
}
