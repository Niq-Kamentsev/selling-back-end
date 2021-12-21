package com.main.sellplatform.entitymanager.testdao;

import com.main.sellplatform.entitymanager.EntityManager;
import com.main.sellplatform.entitymanager.analyzer.Queries;
import com.main.sellplatform.entitymanager.testobj.Lot;
import com.main.sellplatform.entitymanager.testobj.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class UserDao2 {
    private final EntityManager entityManager;
    private final Queries queries;

    @Autowired
    public UserDao2(EntityManager entityManager, Queries queries) {
        this.entityManager = entityManager;
        this.queries = queries;
    }

    public User getUserByEmail(String email) {
        List<Object> statements = new ArrayList<>();
        statements.add(email);
        Object[] o = entityManager.getAllObjects(User.class, queries.whereByUserEmail(), statements);
        if (o == null) return null;
        return (User) o[0];
    }

    public User getUser(Long id) {
        List<Object> statements = new ArrayList<>();
        statements.add(id);
        Object[] o = entityManager.getAllObjects(User.class, queries.whereByUserId(), statements);
        if (o == null) return null;
        return (User) o[0];
    }

    public List<User> getUsers() {
        Object[] objects = entityManager.getAllObjects(User.class, null, null);
        if (objects == null) return null;
        User[] users = new User[objects.length];
        for (int i = 0; i < objects.length; ++i) {
            users[i] = (User) objects[i];
        }
        return Arrays.asList(users);
    }
}
