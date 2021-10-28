package com.main.sellplatform.persistence.entity;


import com.main.sellplatform.persistence.model.Role;

import javax.validation.constraints.*;

public class User {
    private Integer id;
    @NotEmpty
    @Size(min = 1, max = 100)
    @Email
    private String email;
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",message = "password is incorrect ,  a digit must occur at least once" +
            "a lower case letter must occur at least once" +
            "an upper case letter must occur at least once" +
            "a special character must occur at least once" +
            "no whitespace allowed in the entire string ")
    private String password;
    @NotEmpty
    @Size(min = 3 , max = 20, message =  "first name is incorrect ,at least 3 characters")
    private String firstName;
    @NotEmpty
    @Size(min = 3 , max = 20, message =  "last name is incorrect ,at least 3 characters")
    private String lastName;
    private Role role;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
