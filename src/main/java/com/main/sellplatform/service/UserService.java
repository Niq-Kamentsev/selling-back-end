package com.main.sellplatform.service;

import com.main.sellplatform.persistence.dao.UserDao;
import com.main.sellplatform.persistence.dao.UserJdbcTemplateDao;
import com.main.sellplatform.persistence.entity.User;
import com.main.sellplatform.security.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Security;
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

    public User getUser(Integer id){
        User user = userDao.getUser(id);
        if(Objects.isNull(user.getId())) return null;
        return user;
    }

    public void saveUser(User user){
        userDao.saveUser(user);
    }
    public void deleteUser(Integer id){
        userDao.deleteUser(id);
    }
    public User getUserByEmail(String email){
        return userDao.getUserByEmail(email);
    }
}
