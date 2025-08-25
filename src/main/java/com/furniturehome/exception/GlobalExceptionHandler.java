package com.furniturehome.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 200 OK
    @ExceptionHandler(OkResponse.class)
    public ResponseEntity<String> handleOk(OkResponse ex) {
        return ResponseEntity.ok(ex.getMessage());
    }

    // 201 Created
    @ExceptionHandler(CreatedResponse.class)
    public ResponseEntity<String> handleCreated(CreatedResponse ex) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ex.getMessage());
    }

    // 204 No Content
    @ExceptionHandler(NoContentResponse.class)
    public ResponseEntity<Void> handleNoContent(NoContentResponse ex) {
        return ResponseEntity.noContent().build();
    }

    // 400 Bad Request
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> handleBadRequest(BadRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    // 404 Not Found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    // 500 Internal Server Error (catch-all)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Internal server error: " + ex.getMessage());
    }
}
