package com.example.user_test.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ConflictResourceException extends RuntimeException{
    public ConflictResourceException(String message) {
        super(message);
    }
}
