package org.choon.crudprc.exception;

public class LoginException extends RuntimeException{

    public LoginException(String message) {
        super(message);
    }

    public LoginException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
