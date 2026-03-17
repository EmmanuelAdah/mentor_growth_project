package com.server.mentorgrowth.utils;

import com.server.mentorgrowth.exceptions.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.Map;

@RestControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(IncompleteTransactionException.class)
    public Map<String, Object> handleIncompleteTransactionException(IncompleteTransactionException ex) {
        return Map.of("message", ex.getMessage());
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
            InvalidRoleException.class
    })
    public ResponseEntity<Map<String, Object>> handleInvalidFieldExceptions(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler(NotExistingMentorshipException.class)
    public Map<String, Object> handleNotExistingMentorshipException(NotExistingMentorshipException ex) {
        return Map.of("message", ex.getMessage());
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<Map<String, Object>> handleUserAlreadyExistException(UserAlreadyExistException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler({
            NotificationNotFoundException.class,
            NoReviewFoundException.class,
            MentorshipNotFoundException.class,
            NoPaymentFoundException.class
    })
    public ResponseEntity<Map<String, Object>> handleNotFoundExceptions(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolation(DataIntegrityViolationException ex) {

        String message = "Database constraint violation";

        if (ex.getRootCause() != null) {
            message = ex.getRootCause().getMessage();
        }

        return ResponseEntity.badRequest().body(
                Map.of(
                        "error", "DATA_INTEGRITY_VIOLATION",
                        "message", message
                )
        );
    }
}
