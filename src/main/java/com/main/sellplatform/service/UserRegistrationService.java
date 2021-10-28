package com.main.sellplatform.service;

import com.main.sellplatform.persistence.dao.UserDao;
import com.main.sellplatform.persistence.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Objects;

@Service
public class UserRegistrationService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UserRegistrationService(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean registrationUser(User user){
        Assert.notNull(user, "user is empty");
        if (UserByEmailIsEmpty(user.getEmail()))
            return false;
        String password = user.getPassword();
        user.setPassword(passwordEncoder.encode(password));
        userDao.saveUser(user);
        return true;
    }

    private boolean UserByEmailIsEmpty(String email){
        User userByEmail = userDao.getUserByEmail(email);
        return !Objects.isNull(userByEmail.getId());
    }
}
