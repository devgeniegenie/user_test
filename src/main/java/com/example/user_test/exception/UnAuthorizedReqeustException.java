package com.example.user_test.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnAuthorizedReqeustException extends RuntimeException{
    public UnAuthorizedReqeustException(String message) {
        super(message);
    }
}
