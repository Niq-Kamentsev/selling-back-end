package com.main.sellplatform.service;

import com.main.sellplatform.persistence.dao.UserDao;
import com.main.sellplatform.persistence.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserService {
    private final UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public List<User> getUsers() {
        return userDao.getUsers();
    }

    public User getUser(Long id){
        User user = userDao.getUser(id);
        if (Objects.isNull(user.getId())) return null;
        return user;
    }

    public void saveUser(User user){
        userDao.saveUser(user);
    }

    public void deleteUser(Long id){
        userDao.deleteUser(id);
    }

    public User getUserByEmail(String email){
        return userDao.getUserByEmail(email);
    }
}
