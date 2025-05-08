package com.kiendh.springsecurity.exception;

import com.kiendh.springsecurity.infrastructure.response.SystemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Objects;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalHandlerException {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<SystemResponse> handleCustomException(CustomException e) {
        SystemResponse systemResponse = new SystemResponse();
        systemResponse.setTimestamp(LocalDateTime.now());
        systemResponse.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        systemResponse.setMessage(e.getMessage());
        systemResponse.setData(null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(systemResponse);
    }

    @ExceptionHandler(DuplicateRequestException.class)
    public ResponseEntity<SystemResponse> handleDuplicateRequestException(DuplicateRequestException e) {
        SystemResponse systemResponse = new SystemResponse();
        systemResponse.setTimestamp(LocalDateTime.now());
        systemResponse.setCode(HttpStatus.BAD_REQUEST.value());
        systemResponse.setMessage(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(systemResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<SystemResponse> handleValidationException(MethodArgumentNotValidException e) {
        SystemResponse systemResponse = new SystemResponse();
        systemResponse.setTimestamp(LocalDateTime.now());
        systemResponse.setCode(HttpStatus.BAD_REQUEST.value());
        systemResponse.setMessage(Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(systemResponse);
    }
}
