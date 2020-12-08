package com.mikehenry.rabbitmqsimpledatapublisher.exceptions;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class ValidationException extends ResponseEntityExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                            HttpStatus status, WebRequest request) {

        Map<String, Object> body = new HashMap<>();
        body.put("statusCode", HttpStatus.BAD_REQUEST.value());

        List<String> errorDetails = new ArrayList<>();
        for (ObjectError error: ex.getBindingResult().getAllErrors()) {
            errorDetails.add(error.getDefaultMessage());

            body.put("message", error.getDefaultMessage());
        }

        body.put("errors", errorDetails);

        return new ResponseEntity<>(body, status);
    }
}
