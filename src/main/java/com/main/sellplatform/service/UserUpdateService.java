package com.main.sellplatform.service;

import com.main.sellplatform.exception.userexception.EmailException;
import com.main.sellplatform.exception.userexception.UserNotFoundByEmailException;
import com.main.sellplatform.exception.userexception.UserPasswordException;
import com.main.sellplatform.persistence.dao.UserDao;
import com.main.sellplatform.persistence.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
public class UserUpdateService {
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UserUpdateService(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    public void updateUserPassword(User user, String newPassword, String oldPassword) throws UserPasswordException {
        if (!passwordEncoder.matches(oldPassword, user.getPassword())){
            throw new UserPasswordException("old password incorrect");
        }
        if (passwordEncoder.matches(newPassword, user.getPassword()))
            throw new UserPasswordException("The new password must be different from the old one");
        user.setPassword(passwordEncoder.encode(newPassword));
        userDao.saveUser(user);

    }

    public void updateUserEmail(User user, String newEmail) throws EmailException{
        System.out.println(user.getId());
        User userByEmail = userDao.getUserByEmail(newEmail);
        if(!Objects.isNull(userByEmail)){
            throw new EmailException("such email already exists", newEmail);
        }
        System.out.println(user);
        user.setEmail(newEmail);
        userDao.saveUser(user);
    }

    public User getUserByEmail (String email) throws UserNotFoundByEmailException{
        User userByEmail = userDao.getUserByEmail(email);
        if (userByEmail.getId() == null)
            throw new UserNotFoundByEmailException("user with such mail does not exist", email);
        return userByEmail;
    }


}
