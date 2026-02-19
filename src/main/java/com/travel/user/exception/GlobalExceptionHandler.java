package com.travel.user.exception;

import com.travel.user.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handle(Exception e) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(
                        e.getMessage(),
                        System.currentTimeMillis(),
                        HttpStatus.BAD_REQUEST.value(),
                        "Error"
                ));
    }

    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<ErrorResponse> handleDuplicate(DuplicateUserException e) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(
                        e.getMessage(),
                        System.currentTimeMillis(),
                        HttpStatus.CONFLICT.value(),
                        "Duplicate User"
                ));
    }


}
