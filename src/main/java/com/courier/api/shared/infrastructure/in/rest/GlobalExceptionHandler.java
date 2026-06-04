package com.courier.api.shared.infrastructure.in.rest;

import com.courier.api.shared.domain.exception.ConflictException;
import com.courier.api.shared.domain.exception.NotFoundException;
import com.courier.api.shared.domain.exception.ValidationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for REST controllers.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles not found exceptions.
     *
     * @param ex      the not found exception
     * @param request the HTTP servlet request
     * @return a response entity with the error details
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(NotFoundException ex, HttpServletRequest request) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    /**
     * Handles conflict exceptions.
     *
     * @param ex      the conflict exception
     * @param request the HTTP servlet request
     * @return a response entity with the error details
     */
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiErrorResponse> handleConflict(ConflictException ex, HttpServletRequest request) {
        return build(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    /**
     * Handles validation exceptions.
     *
     * @param ex      the validation exception
     * @param request the HTTP servlet request
     * @return a response entity with the error details
     */
    @ExceptionHandler({
            ValidationException.class,
            MethodArgumentNotValidException.class,
            ConstraintViolationException.class
    })
    public ResponseEntity<ApiErrorResponse> handleValidation(Exception ex, HttpServletRequest request) {
        String message = ex instanceof MethodArgumentNotValidException manv
                ? manv.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .orElse("Validation error")
                : ex.getMessage();
        return build(HttpStatus.BAD_REQUEST, message, request);
    }

    /**
     * Handles generic exceptions.
     *
     * @param ex      the exception
     * @param request the HTTP servlet request
     * @return a response entity with the error details
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneric(Exception ex, HttpServletRequest request) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request);
    }

    private ResponseEntity<ApiErrorResponse> build(
            HttpStatus status, String message, HttpServletRequest request
    ) {
        return ResponseEntity.status(status).body(new ApiErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI()
        ));
    }
}
