package com.main.sellplatform.controller.dto.userdto;


import com.main.sellplatform.persistence.entity.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserDto {

    @NotNull(message = "First name can not be empty")
    @Size(min = 3, max = 20, message = "First name has incorrect length")
    private String firstName;
    @NotNull(message = "Last name can not be empty")
    @Size(min = 3, max = 20, message = "Last name has incorrect length")
    private String lastName;
    @NotNull(message = "Email can not be empty")
    @Email(message = "Invalid email address")
    private String email;
    @Email(message = "Invalid email address")
    private String new_email;
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
            message = "Password must be at least 8 characters long, must contain at least one digit, one lower and upper case letter, one special character and no whitespaces allowed.")
    private String password;
    @Pattern(regexp = "^\\+\\d{10,12}$", message = "Invalid phone number")
    private String phoneNumber;


    public UserDto(String firstName, String lastName, String email, String password, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    public String getFistName() {
        return firstName;
    }

    public void setFistName(String fistName) {
        this.firstName = fistName;
    }

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

    public String getNew_email() {
        return new_email;
    }

    public void setNew_email(String new_email) {
        this.new_email = new_email;
    }
}
