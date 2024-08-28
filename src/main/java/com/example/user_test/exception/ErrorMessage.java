package com.example.user_test.exception;

public enum ErrorMessage {
    USER_NOT_FOUND("User not found"),
    INVALID_CREDENTIALS("Invalid credentials"),
    RESOURCE_CONFLICT("Resource already exists"),
    INVALID_REQUEST("Invalid request"),
    UNAUTHORIZED("Unauthorized"),
    ;

    private String message;

    ErrorMessage(String s) {
    }
}
