package com.main.sellplatform.persistence.entity;


import com.main.sellplatform.entitymanager.GeneralObject;
import com.main.sellplatform.entitymanager.annotation.Attribute;
import com.main.sellplatform.entitymanager.annotation.Objtype;
import com.main.sellplatform.entitymanager.annotation.Reference;


import java.util.Date;

@Objtype(12)
public class RefreshToken extends GeneralObject {
    @Reference(attributeId = 61)
    private User user;
    @Attribute(attrTypeId = 62)
    private String token;
    @Attribute(attrTypeId = 63, type = Attribute.ValueType.DATE_VALUE)
    private Date expiryDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
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

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }


    @Override
    public String toString() {
        return "RefreshToken{" +
                "user=" + user +
                ", token='" + token + '\'' +
                ", expiryDate=" + expiryDate +
                '}';
    }
}
