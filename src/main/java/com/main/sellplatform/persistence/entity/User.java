package com.main.sellplatform.persistence.entity;

import com.main.sellplatform.entitymanager.GeneralObject;
import com.main.sellplatform.entitymanager.annotation.Attribute;
import com.main.sellplatform.entitymanager.annotation.Objtype;
import com.main.sellplatform.persistence.entity.enums.Role;

import java.util.Objects;

@Objtype(1)
public class User extends GeneralObject {

    @Attribute(attrTypeId = 1)
    private String email;
    @Attribute(attrTypeId = 2)
    private String password;
    @Attribute(attrTypeId = 4)
    private String firstName;
    @Attribute(attrTypeId = 5)
    private String lastName;
    @Attribute(attrTypeId = 3)
    private String phoneNumber;
    @Attribute(attrTypeId = 7)
    private Role role;
    @Attribute(attrTypeId = 65)
    private String activationCode;
    @Attribute(attrTypeId = 64)
    private Boolean isActive;

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

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
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", role=" + role +
                ", activationCode='" + activationCode + '\'' +
                ", isActive=" + isActive +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return isActive == user.isActive && email.equals(user.email) && password.equals(user.password) && firstName.equals(user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(phoneNumber, user.phoneNumber) && role == user.role && Objects.equals(activationCode, user.activationCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, password, firstName, lastName, phoneNumber, role, activationCode, isActive);
    }
}
