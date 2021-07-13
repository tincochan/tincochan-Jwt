package com.example.tincochanjwt.exception;

public class RegisterUsernameHasBeenExists extends Exception{
    private String message;
    public RegisterUsernameHasBeenExists(String message) {
        this.message = message;
    }
}
