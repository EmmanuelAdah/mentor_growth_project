package com.server.mentorgrowth.utils;

import com.server.mentorgrowth.exceptions.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationException(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Validation failed", errors));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<String>> handleConstraintViolation(ConstraintViolationException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(IncompleteTransactionException.class)
    public ResponseEntity<ApiResponse<Void>> handleIncompleteTransactionException(IncompleteTransactionException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler({
            UserNotFoundException.class,
            InvalidPaymentReferenceException.class,
            InvalidEmailFormatException.class,
            InvalidNameFormatException.class,
            InvalidPasswordFormatException.class,
            InvalidUserIdentityException.class,
            InvalidPaymentIdentityException.class,
            InvalidCredentialsException.class,
            InvalidRoleException.class,
            InvalidMentorshipException.class,
            InvalidPasswordLengthException.class
    })
    public ResponseEntity<ApiResponse<Void>> handleInvalidFieldExceptions(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(NotExistingMentorshipException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotExistingMentorshipException(NotExistingMentorshipException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<ApiResponse<Void>> handleUserAlreadyExistException(UserAlreadyExistException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler({
            NotificationNotFoundException.class,
            NoReviewFoundException.class,
            MentorshipNotFoundException.class,
            NoPaymentFoundException.class,
            PaymentNotFoundException.class
    })
    public ResponseEntity<ApiResponse<Void>> handleNotFoundExceptions(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler({
            DeleteNotificationException.class,
            ServerConnectionException.class
    })
    public ResponseEntity<ApiResponse<Void>> handleServerExceptions(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleDataIntegrityViolation(
            DataIntegrityViolationException ex) {
        String message = "Database constraint violation";
        if (ex.getRootCause() != null) {
            message = ex.getRootCause().getMessage();
        }
        return ResponseEntity
                .badRequest()
                .body(ApiResponse.error(message, Map.of("error", "DATA_INTEGRITY_VIOLATION")));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(ex.getMessage() != null ? ex.getMessage() : "Unexpected server error"));
    }
}
