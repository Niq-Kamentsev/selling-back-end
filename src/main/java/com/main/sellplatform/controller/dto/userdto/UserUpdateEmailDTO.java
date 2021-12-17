package com.main.sellplatform.controller.dto.userdto;

import javax.validation.constraints.Email;

public class UserUpdateEmailDTO {

    @Email(message = "Invalid email address")
    private String newEmail;




    public String getNewEmail() {
        return newEmail;
    }

    public void setNewEmail(String newEmail) {
        this.newEmail = newEmail;
    }
}
