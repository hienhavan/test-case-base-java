package org.example.demomogodb.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String BAD_REQUEST_MESSAGE = "Invalid request";
    private static final String SERVER_ERROR_MESSAGE = "Server error";
    private static final String UNAUTHORIZED_MESSAGE = "Unauthorized access";
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String handleUnauthorizedError(UnauthorizedException ex) {
        return UNAUTHORIZED_MESSAGE + ": " + ex.getMessage();
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBadRequest(RuntimeException ex) {
        return BAD_REQUEST_MESSAGE + ": " + ex.getMessage();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleServerError(Exception ex) {
        return SERVER_ERROR_MESSAGE + ": " + ex.getMessage();
    }
}
