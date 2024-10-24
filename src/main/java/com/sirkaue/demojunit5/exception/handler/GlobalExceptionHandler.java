package com.sirkaue.demojunit5.exception.handler;

import com.sirkaue.demojunit5.exception.EmailUniqueViolationException;
import com.sirkaue.demojunit5.exception.ObjectNotFoundException;
import com.sirkaue.demojunit5.exception.StandardError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<StandardError> objectNotFound(ObjectNotFoundException e, HttpServletRequest request) {
        return createErrorResponse(request, NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(EmailUniqueViolationException.class)
    public ResponseEntity<StandardError> emailUniqueViolationException(EmailUniqueViolationException e, HttpServletRequest request) {
        return createErrorResponse(request, CONFLICT, e.getMessage());
    }

    private ResponseEntity<StandardError> createErrorResponse(HttpServletRequest request, HttpStatus status, String message) {
        StandardError error = new StandardError(request, status, message);
        return ResponseEntity
                .status(status)
                .contentType(APPLICATION_JSON)
                .body(error);
    }
}
