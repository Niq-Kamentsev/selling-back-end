package com.main.sellplatform.exception.userexception;

public class EmailException extends RuntimeException {
    private String email;

    public EmailException(String message, String email) {
        super(message);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
