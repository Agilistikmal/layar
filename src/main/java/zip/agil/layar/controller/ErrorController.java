package zip.agil.layar.controller;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import zip.agil.layar.model.WebResponse;

@RestControllerAdvice
public class ErrorController {

    @ExceptionHandler(ConstraintViolationException.class)
    private ResponseEntity<WebResponse<String>> constraintViolationException(ConstraintViolationException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        WebResponse.<String>builder()
                                .status(HttpStatus.BAD_REQUEST.value())
                                .message(exception.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(ResponseStatusException.class)
    private ResponseEntity<WebResponse<String>> responseStatusException(ResponseStatusException exception) {
        return ResponseEntity
                .status(exception.getStatusCode())
                .body(
                        WebResponse.<String>builder()
                                .status(exception.getStatusCode().value())
                                .message(exception.getMessage())
                                .build()
                );
    }
}
