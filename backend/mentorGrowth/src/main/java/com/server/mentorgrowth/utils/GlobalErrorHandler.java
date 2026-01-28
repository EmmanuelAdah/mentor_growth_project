package com.server.mentorgrowth.utils;

import com.server.mentorgrowth.exceptions.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.Map;

@RestControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(InvalidCredentialsException.class)
    public Map<String, Object> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        return Map.of("message", ex.getMessage());
    }

    @ExceptionHandler(IncompleteTransactionException.class)
    public Map<String, Object> handleIncompleteTransactionException(IncompleteTransactionException ex) {
        return Map.of("message", ex.getMessage());
    }

    @ExceptionHandler(InvalidPaymentIdentityException.class)
    public Map<String, Object> handleInvalidPaymentIdentityException(InvalidPaymentIdentityException ex) {
        return Map.of("message", ex.getMessage());
    }

    @ExceptionHandler(InvalidPaymentReferenceException.class)
    public Map<String, Object> handleInvalidPaymentReferenceException(InvalidPaymentReferenceException ex) {
        return Map.of("message", ex.getMessage());
    }

    @ExceptionHandler(NoPaymentFoundException.class)
    public Map<String, Object> handleNoPaymentFoundException(NoPaymentFoundException ex) {
        return Map.of("message", ex.getMessage());
    }

    @ExceptionHandler(InvalidEmailFormatException.class)
    public Map<String, Object> handleInvalidEmailFormatException(InvalidEmailFormatException ex) {
        return Map.of("message", ex.getMessage());
    }

    @ExceptionHandler(InvalidUserIdentityException.class)
    public Map<String, Object> handleInvalidUserIdentityException(InvalidUserIdentityException ex) {
        return Map.of("message", ex.getMessage());
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
    public Map<String, Object> handleUserAlreadyExistException(UserAlreadyExistException ex) {
        return Map.of("message", ex.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public Map<String, Object> handleUserNotFoundException(UserNotFoundException ex) {
        return Map.of("message", ex.getMessage());
    }

    @ExceptionHandler(NotificationNotFoundException.class)
    public Map<String, Object> handleNotificationNotFoundException(NotificationNotFoundException ex) {
        return Map.of("message", ex.getMessage());
    }

    @ExceptionHandler(NoReviewFoundException.class)
    public Map<String, Object> handleNoReviewFoundException(NoReviewFoundException ex) {
        return Map.of("message", ex.getMessage());
    }

    @ExceptionHandler(MentorshipNotFoundException.class)
    public Map<String, Object> handleMentorshipNotFoundException(MentorshipNotFoundException ex) {
        return Map.of("message", ex.getMessage());
    }
}
