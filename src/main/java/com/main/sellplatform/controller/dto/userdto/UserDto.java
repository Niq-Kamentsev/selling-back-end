package com.main.sellplatform.controller.dto.userdto;


import com.main.sellplatform.persistence.entity.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserDto {

    @NotNull
    @Size(min = 3, max = 20, message = "First name has incorrect length")
    private String firstName;
    @NotNull
    @Size(min = 3, max = 20, message = "Last name has incorrect length")
    private String lastName;
    @NotNull
    @Email
    private String email;
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
            message = "Password is incorrect, a digit must occur at least once." +
                    "A lower case letter must occur at least once." +
                    "An upper case letter must occur at least once." +
                    "A special character must occur at least once." +
                    "No whitespace allowed in the entire string.")
    private String password;
    @Size(min = 10, max = 20, message = "Phone number has incorrect length")
    private String phoneNumber;

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public User getUser(){
        User user = new User();
        user.setFirstName(this.firstName);
        user.setLastName(this.lastName);
        user.setEmail(this.email);
        user.setPassword(this.password);
        user.setPhoneNumber(this.phoneNumber);
        return user;

    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
