package com.main.sellplatform.entitymanager.testdao;

import com.main.sellplatform.entitymanager.EntityManager;
import com.main.sellplatform.entitymanager.testobj.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;

@Component
public class UserDao2 {
    private final EntityManager entityManager;

    @Autowired
    public UserDao2(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public User getUserByEmail(String email) {
        Object[] o = entityManager.getAllObjects(User.class, "OBJ_1ATTR_1 = '" + email + "'");
        if (o == null) return null;
        return (User) o[0];
    }

    public User getUser(Long id) {
        Object[] o = entityManager.getAllObjects(User.class, "OBJ_1ID = '" + id + "'");
        if (o == null) return null;
        return (User) o[0];
    }
}
