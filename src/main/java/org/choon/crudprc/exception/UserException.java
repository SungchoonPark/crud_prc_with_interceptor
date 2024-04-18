package org.choon.crudprc.exception;

public class UserException extends RuntimeException{
    public UserException(String message) {
        super(message);
    }

    public UserException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
