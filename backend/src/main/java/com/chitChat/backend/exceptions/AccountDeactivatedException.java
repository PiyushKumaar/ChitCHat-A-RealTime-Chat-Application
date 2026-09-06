package com.chitChat.backend.exceptions;

public class AccountDeactivatedException extends RuntimeException{
    public AccountDeactivatedException(String message){
        super(message);
    }
}
