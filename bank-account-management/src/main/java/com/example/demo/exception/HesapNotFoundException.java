package com.example.demo.exception;

public class HesapNotFoundException extends RuntimeException {
    public HesapNotFoundException(String message) {
        super(message);
    }
}