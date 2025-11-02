package com.ldal.pigeonapp;

public class InvalidEmailException extends RuntimeException
{
    public InvalidEmailException(String message) {
        super(message);
    }
}
