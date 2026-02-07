package com.server.mentorgrowth.utils;

import com.server.mentorgrowth.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.Map;

@RestControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler(IncompleteTransactionException.class)
    public Map<String, Object> handleIncompleteTransactionException(IncompleteTransactionException ex) {
        return Map.of("message", ex.getMessage());
    }

    @ExceptionHandler({
            InvalidPaymentReferenceException.class,
            InvalidEmailFormatException.class,
            InvalidUserIdentityException.class,
            InvalidPaymentIdentityException.class
    })
    public ResponseEntity<Map<String, Object>> handleInvalidPaymentReferenceException(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler(NotExistingMentorshipException.class)
    public Map<String, Object> handleNotExistingMentorshipException(NotExistingMentorshipException ex) {
        return Map.of("message", ex.getMessage());
    }

    @ExceptionHandler(InvalidRoleException.class)
    public Map<String, Object> handleInvalidRoleException(InvalidRoleException ex) {
        return Map.of("message", ex.getMessage());
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<Map<String, Object>> handleUserAlreadyExistException(UserAlreadyExistException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler({
            UserNotFoundException.class,
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
}
