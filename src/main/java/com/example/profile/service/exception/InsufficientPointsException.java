package com.example.profile.service.exception;

public class InsufficientPointsException extends RuntimeException {

    public InsufficientPointsException() {
        super("Insufficient loyalty points");
    }
}
