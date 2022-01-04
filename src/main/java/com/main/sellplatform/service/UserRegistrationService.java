package com.main.sellplatform.service;

import com.main.sellplatform.exception.userexception.UserNotFoundByEmailException;
import com.main.sellplatform.persistence.dao.UserDao;
import com.main.sellplatform.persistence.entity.User;
import com.main.sellplatform.persistence.entity.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Objects;
import java.util.UUID;


@Service
public class UserRegistrationService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final MailSender mailSender;


    @Autowired
    public UserRegistrationService(UserDao userDao, PasswordEncoder passwordEncoder, MailSender mailSender) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }

    public boolean registrationUser(User user){
        Assert.notNull(user, "user is empty");
        if (UserByEmailIsEmpty(user.getEmail()))
            throw new UserNotFoundByEmailException("user not found", user.getEmail());
        String password = user.getPassword();
        user.setPassword(passwordEncoder.encode(password));
        user.setActivationCode(UUID.randomUUID().toString());
        user.setRole(Role.ADMIN);
        user.setActive(false);
        userDao.saveUser(user);
        String message = String.format("Hello , %s! \n" +
                "Welcome to our project .Please visit next link: http://localhost:8080/api/v1/registration/activation%s",user.getFirstName(), user.getActivationCode());
        mailSender.send(user.getEmail(), "Activated code ",message );

        return true;
    }

    public boolean activeUser(String code){
        User user = userDao.getUserByActivatedCode(code);
        if(user.getId() == null)
            return false;
        user.setActivationCode("activated");
        user.setActive(true);
        userDao.updateActiveUser(user);
        return true;

    }

    private boolean UserByEmailIsEmpty(String email){
        com.main.sellplatform.entitymanager.testobj.User userByEmail = userDao.getTestUserByEmail(email);
        if(userByEmail==null){
            return false;
        }
        return !Objects.isNull(userByEmail.getId());
    }


}
