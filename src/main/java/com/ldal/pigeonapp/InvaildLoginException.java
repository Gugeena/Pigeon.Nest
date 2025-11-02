package com.ldal.pigeonapp;

public class InvaildLoginException extends RuntimeException
{
    public InvaildLoginException(String message) {
        super(message);
    }
}
