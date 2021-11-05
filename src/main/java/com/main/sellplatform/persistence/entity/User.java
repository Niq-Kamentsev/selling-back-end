package com.main.sellplatform.persistence.entity;
import com.main.sellplatform.persistence.entity.enums.Role;

import javax.validation.constraints.*;

public class User {
    private Long id;
    @NotNull
    @Size(min = 1, max = 100)
    @Email
    private String email;
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
            message = "Password is incorrect, a digit must occur at least once." +
                    "A lower case letter must occur at least once." +
                    "An upper case letter must occur at least once." +
                    "A special character must occur at least once." +
                    "No whitespace allowed in the entire string.")
    private String password;
    @NotNull
    @Size(min = 3, max = 20, message = "First name has incorrect length")
    private String firstName;
    @NotEmpty
    @Size(min = 3, max = 20, message = "Last name has incorrect length")
    private String lastName;
    @Size(min = 10, max = 20, message = "Phone number has incorrect length")
    private String phoneNumber;
    private Role role;
    private String activationCode;

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    private boolean isActive;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", role=" + role +
                ", active=" + isActive +
                '}';
    }
}
