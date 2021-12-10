package com.main.sellplatform.entitymanager.testdao;

import com.main.sellplatform.entitymanager.EntityManager;
import com.main.sellplatform.entitymanager.testobj.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserDao2 {
    private final EntityManager entityManager;

    @Autowired
    public UserDao2(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public User getUserByEmail(String email) {
        List<Object> statements = new ArrayList<>();
        statements.add(email);
        Object[] o = entityManager.getAllObjects(User.class, "OBJ_1ATTR_1 = ?",statements);
        if (o == null) return null;
        return (User) o[0];
    }

    public User getUser(Long id) {
        List<Object> statements = new ArrayList<>();
        statements.add(id);
        Object[] o = entityManager.getAllObjects(User.class, "OBJ_1ID = ?",statements);
        if (o == null) return null;
        return (User) o[0];
    }
}
