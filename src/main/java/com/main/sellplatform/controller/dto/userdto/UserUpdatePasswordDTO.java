package com.main.sellplatform.controller.dto.userdto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class UserUpdatePasswordDTO {

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
            message = "new password is incorrect, a digit must occur at least once." +
                    "A lower case letter must occur at least once." +
                    "An upper case letter must occur at least once." +
                    "A special character must occur at least once." +
                    "No whitespace allowed in the entire string.")
    private String newPassword;
    @NotNull(message = "New password can not be empty")
    private String oldPassword;


    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
}
