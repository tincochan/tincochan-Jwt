package com.example.tincochanjwt.exception;

public class RegisterFailureException extends Exception{

    private String message;
    public RegisterFailureException() {
        super();
    }
    public RegisterFailureException(String message) {
        this.message = message;
    }
}