package com.main.sellplatform.persistence.dao;

import com.google.common.collect.Lists;
import com.main.sellplatform.entitymanager.EntityManager;
import com.main.sellplatform.persistence.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class UserDao {
    EntityManager entityManager;

    @Autowired
    public UserDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<User> getUsers(){
        Object[] allObjects = entityManager.getAllObjects(User.class);
        User[] users = Arrays.stream(allObjects).toArray(User[]::new);
        return Lists.newArrayList(users);
    }


    public User getUser(Long id){
        return entityManager.getObjectById(User.class,  id, null, null);

    }

    public void saveUser(User user) {
        entityManager.merge(user);

    }

    public com.main.sellplatform.entitymanager.testobj.User getTestUserByEmail(String email) {
        return entityManager.getObjectByWhere(com.main.sellplatform.entitymanager.testobj.User.class, "(OBJ_1ATTR_1 = ?)", Collections.singletonList(email));
    }
    public User getUserByEmail(String email) {
        return entityManager.getObjectByWhere(User.class, "(OBJ_1ATTR_1 = ?)", Collections.singletonList(email));
    }

    public User getUserByActivatedCode(String code){
        return entityManager.getObjectByWhere(User.class, "(OBJ_1ATTR_65 = ?)", Collections.singletonList(code));

    }

    public User updateActiveUser(User user){
        return entityManager.merge(user);
    }


}
