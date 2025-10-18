package com.thirdeye3.eurekaserver.exceptions.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.thirdeye3.eurekaserver.dtos.Response;
import com.thirdeye3.eurekaserver.exceptions.EurekaSummaryException;


@ControllerAdvice
public class GlobalExceptionHandler {
	
    @ExceptionHandler(EurekaSummaryException.class)
    public ResponseEntity<Response<Void>> handleTopicException(EurekaSummaryException ex) {
        return buildResponse(false, HttpStatus.NOT_FOUND.value(), ex.getMessage(), null);
    }
    
    private <T> ResponseEntity<Response<T>> buildResponse(
            boolean success, int errorCode, String errorMessage, T body) {
        return ResponseEntity
                .status(errorCode)
                .body(new Response<>(success, errorCode, errorMessage, body));
    }
}

