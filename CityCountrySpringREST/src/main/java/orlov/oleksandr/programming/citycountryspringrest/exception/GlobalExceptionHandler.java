package orlov.oleksandr.programming.citycountryspringrest.exception;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Global exception handler for REST controllers.
 * This class handles various exceptions and returns appropriate ResponseEntity objects with error details.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handles NullPointerException and returns a ResponseEntity with status code 400 (BAD_REQUEST)
     * and a message indicating the error.
     *
     * @param ex The NullPointerException object
     * @return ResponseEntity with status code 400 and error details
     */
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<?> handleNullPointerException(NullPointerException ex) {
        logException("NullPointer Exception occurred", ex);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "BAD_REQUEST");
        response.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Handles MethodArgumentNotValidException and returns a ResponseEntity with status code 400 (BAD_REQUEST)
     * and a list of validation errors.
     *
     * @param ex The MethodArgumentNotValidException object
     * @return ResponseEntity with status code 400 and validation errors
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        logException(ex.getMessage(), ex);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "BAD_REQUEST");

        List<String> errors = new ArrayList<>();
        for(var error : ex.getBindingResult().getAllErrors()) {
            errors.add(error.getDefaultMessage());
        }

        response.put("errors", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Handles EntityNotFoundException and returns a ResponseEntity with status code 404 (NOT_FOUND)
     * and a message indicating the error.
     *
     * @param ex The EntityNotFoundException object
     * @return ResponseEntity with status code 404 and error details
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException ex) {
        logException("EntityNotFoundException occurred", ex);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "NOT_FOUND");
        response.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * Handles IllegalArgumentException and returns a ResponseEntity with status code 400 (BAD_REQUEST)
     * and a message indicating the error.
     *
     * @param ex The IllegalArgumentException object
     * @return ResponseEntity with status code 400 and error details
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException ex) {
        logException("IllegalArgumentException occurred", ex);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "BAD_REQUEST");
        response.put("errorMessage", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Handles generic Exception and returns a ResponseEntity with status code 500 (INTERNAL_SERVER_ERROR)
     * and a message indicating the error.
     *
     * @param ex The Exception object
     * @return ResponseEntity with status code 500 and error details
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex) {
        logException("Exception occurred", ex);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "INTERNAL_SERVER_ERROR");
        response.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    /**
     * Handles RuntimeException and returns a ResponseEntity with status code 500 (INTERNAL_SERVER_ERROR)
     * and a message indicating the error.
     *
     * @param ex The RuntimeException object
     * @return ResponseEntity with status code 500 and error details
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException ex) {
        logException("RuntimeException occurred", ex);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "INTERNAL_SERVER_ERROR");
        response.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    /**
     * Logs the exception with appropriate message.
     *
     * @param message The log message
     * @param ex      The Exception object
     */
    private void logException(String message, Exception ex) {
        log.error(message, ex);
    }
}