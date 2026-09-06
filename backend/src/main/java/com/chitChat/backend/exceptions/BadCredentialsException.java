package com.chitChat.backend.exceptions;

public class BadCredentialsException extends RuntimeException {

    public BadCredentialsException(String message){
        super(message);
    }
}
