package back.activitymanager.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(
            MethodArgumentNotValidException ex
    ) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("Time", LocalDateTime.now());

        List<String> errors = ex.getBindingResult()
                .getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        body.put("Errors", errors);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> handlerEntityNotFoundException(EntityNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<String> handlerRegistrationException(RegistrationException ex) {
        return new ResponseEntity<>(ex.getMessage(),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<String> handlerAuthorizationException(AuthorizationDeniedException ex) {
        return new ResponseEntity<>("Access denied",
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<String> handlerDropboxException(DropboxProcessException ex) {
        return new ResponseEntity<>("Dropbox problem: " + ex.getMessage(),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllExceptions(Exception ex) {
        return new ResponseEntity<>(
                "An unexpected error occurred: "
                        + ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleDataIntegrityViolation(
            DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Data conflict: " + ex.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> handleNoAccessException(
            NoAccessException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("No access: " + ex.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> handleLocalNotFoundException(
            LocalNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Local error: " + ex.getMessage());
    }
}
