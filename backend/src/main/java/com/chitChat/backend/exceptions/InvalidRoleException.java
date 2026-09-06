package com.chitChat.backend.exceptions;

public class InvalidRoleException extends RuntimeException{

    public InvalidRoleException(String message){
        super(message);
    }
}
