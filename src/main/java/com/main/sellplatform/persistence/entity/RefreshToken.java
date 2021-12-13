package com.main.sellplatform.persistence.entity;


import com.main.sellplatform.entitymanager.testobj.User;

import java.time.LocalDate;

public class RefreshToken {
    private Long id;
    private com.main.sellplatform.entitymanager.testobj.User user;
    private String token;
    private LocalDate expiryDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public com.main.sellplatform.entitymanager.testobj.User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }
}
