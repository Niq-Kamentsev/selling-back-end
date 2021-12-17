package com.main.sellplatform.service;

import com.main.sellplatform.controller.dto.userdto.UserUpdateInfoDTO;
import com.main.sellplatform.exception.userexception.EmailException;
import com.main.sellplatform.exception.userexception.UserNotFoundByEmailException;
import com.main.sellplatform.exception.userexception.UserPasswordException;
import com.main.sellplatform.persistence.dao.UserDao;
import com.main.sellplatform.persistence.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class UserUpdateService {
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final MailSender mailSender;


    @Autowired
    public UserUpdateService(UserDao userDao, PasswordEncoder passwordEncoder, MailSender mailSender) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }

    public void updateUserPassword(User user, String newPassword, String oldPassword) throws UserPasswordException {
        System.out.println("1");
        if (!passwordEncoder.matches(oldPassword, user.getPassword())){
            throw new UserPasswordException("old password incorrect");
        }else if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new UserPasswordException("The new password must be different from the old one");
        }
        System.out.println("2");
        user.setPassword(passwordEncoder.encode(newPassword));
        userDao.updatePasswordUser(user);

    }

    public void updateUserEmail(User user, String newEmail) throws EmailException{
        User userByEmail = userDao.getUserByEmail(newEmail);
//        if(userByEmail.getId() != null){
//            throw new EmailException("such email already exists", newEmail);
//        }
        user.setActivationCode(UUID.randomUUID().toString());
        user.setNewEmail(newEmail);
        userDao.updateNewEmail(user, newEmail);
        String message = String.format("Hello, %s \n" +
                "There is an attempt to change your email address to %s. If it isn't you please ignore this message.\n"+
                "If it is you, you can activate new email by visiting this link: http://localhost:8080/api/update/updateEmail/activation%s", user.getFirstName(),user.getNewEmail(), user.getActivationCode());
        mailSender.send(user.getEmail(),"New email activation ", message);
    }

    public User getUserByEmail (String email) throws UserNotFoundByEmailException{
        User userByEmail = userDao.getUserByEmail(email);
        System.out.println("got user");
//        if (userByEmail.getId() == null)
//            throw new UserNotFoundByEmailException("user with such mail does not exist", email);
        return userByEmail;
    }
    public boolean activeNewEmail(String code){
        User user = userDao.getUserByActivatedCode(code);
        if(user.getId() == null)
            return false;
        user.setActivationCode("activated");
        user.setEmail(user.getNewEmail());
        user.setNewEmail(null);
        //user.setActive(true);
        userDao.updateActiveUserEmail(user);
        return true;

    }
    public void updateUserInfo(User user, UserUpdateInfoDTO user_info){
        user.setFirstName(user_info.getFirstName());
        user.setLastName(user_info.getLastName());
        user.setPhoneNumber(user_info.getPhoneNumber());
        userDao.updateUserInfo(user);

    }


}
