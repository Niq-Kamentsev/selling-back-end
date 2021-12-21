package com.main.sellplatform.service;

import com.main.sellplatform.entitymanager.testdao.UserDao2;
import com.main.sellplatform.persistence.dao.UserDao;
import com.main.sellplatform.persistence.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserService {
    private final UserDao userDao;
    private final UserDao2 userDao2;

    @Autowired
    public UserService(UserDao userDao, UserDao2 userDao2) {
        this.userDao = userDao;
        this.userDao2 = userDao2;
    }

    public List<com.main.sellplatform.entitymanager.testobj.User> getUsers() {
        return userDao2.getUsers();
    }

    public User getUser(Long id) {
        User user = userDao.getUser(id);
        if (Objects.isNull(user.getId())) return null;
        return user;
    }

    public void saveUser(User user) {
        userDao.saveUser(user);
    }

    public void deleteUser(Long id) {
        userDao.deleteUser(id);
    }

    public com.main.sellplatform.entitymanager.testobj.User getUserByEmail(String email) {
        return userDao2.getUserByEmail(email);
    }
}
