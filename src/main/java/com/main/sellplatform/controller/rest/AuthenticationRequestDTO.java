package com.main.sellplatform.controller.rest;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class AuthenticationRequestDTO {
    @Email
    private String email;
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
            message = "Password is incorrect, a digit must occur at least once." +
                    "A lower case letter must occur at least once." +
                    "An upper case letter must occur at least once." +
                    "A special character must occur at least once." +
                    "No whitespace allowed in the entire string.")
    private String password;

    public AuthenticationRequestDTO() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
