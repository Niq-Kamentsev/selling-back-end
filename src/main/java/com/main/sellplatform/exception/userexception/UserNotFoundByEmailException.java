package com.main.sellplatform.exception.userexception;

public class UserNotFoundByEmailException extends RuntimeException{

    private String email;

    public UserNotFoundByEmailException(String message, String email) {
        super(message);
        this.email = email;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
