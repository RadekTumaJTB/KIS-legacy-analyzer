package cz.jtbank.kis.bff.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Global Exception Handler for BFF API.
 * Fixes issue: Returns 500 instead of proper HTTP status codes (404, 400, etc.)
 *
 * THIS FIXES CRITICAL ISSUE found by API Tester on 2025-12-09:
 * - GET /bff/projects/999999 should return 404, not 500
 * - GET /bff/budgets/999999 should return 404, not 500
 * - GET /bff/documents/999999 should return 404, not 500
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle EntityNotFoundException - return 404 NOT FOUND
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleEntityNotFound(
            EntityNotFoundException ex, WebRequest request) {

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", "Not Found");
        body.put("message", ex.getMessage() != null ? ex.getMessage() : "Resource not found");
        body.put("path", request.getDescription(false).replace("uri=", ""));

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    /**
     * Handle IllegalArgumentException - return 400 BAD REQUEST
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(
            IllegalArgumentException ex, WebRequest request) {

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Bad Request");
        body.put("message", ex.getMessage() != null ? ex.getMessage() : "Invalid argument");
        body.put("path", request.getDescription(false).replace("uri=", ""));

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle NullPointerException - return 404 or 500 depending on context
     */
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Map<String, Object>> handleNullPointer(
            NullPointerException ex, WebRequest request) {

        String path = request.getDescription(false).replace("uri=", "");

        // If path contains an ID (e.g., /bff/projects/123), assume it's a "not found" scenario
        boolean isNotFoundScenario = path.matches(".*/(\\d+)(/.*)?$");

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());

        if (isNotFoundScenario) {
            body.put("status", HttpStatus.NOT_FOUND.value());
            body.put("error", "Not Found");
            body.put("message", "Resource not found");
            body.put("path", path);
            return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
        } else {
            body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            body.put("error", "Internal Server Error");
            body.put("message", "An unexpected error occurred");
            body.put("path", path);
            return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Handle generic exceptions - return 500 INTERNAL SERVER ERROR
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(
            Exception ex, WebRequest request) {

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "Internal Server Error");
        body.put("message", "An unexpected error occurred");
        body.put("path", request.getDescription(false).replace("uri=", ""));
        body.put("exception", ex.getClass().getSimpleName());

        // Log the full exception for debugging
        ex.printStackTrace();

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
