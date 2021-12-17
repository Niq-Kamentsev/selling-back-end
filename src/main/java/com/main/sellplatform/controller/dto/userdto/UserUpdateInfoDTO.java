package com.main.sellplatform.controller.dto.userdto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserUpdateInfoDTO {
    @NotNull(message = "First name can not be empty")
    @Size(min = 3, max = 20, message = "First name has incorrect length")
    private String firstName;
    @NotNull(message = "Last name can not be empty")
    @Size(min = 3, max = 20, message = "Last name has incorrect length")
    private String lastName;
    @Pattern(regexp = "^\\+\\d{10,12}$", message = "Invalid phone number")
    private String phoneNumber;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
