package com.main.sellplatform.security;

import com.main.sellplatform.persistence.entity.User;
import com.main.sellplatform.persistence.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserDao userDao;


    @Autowired
    public UserDetailsServiceImpl(UserDao userDao) {
        this.userDao = userDao;

    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        //User userByEmail = userDao.getUserByEmail(email);
        User userByEmail = userDao.getUserByEmail(email);
        return SecurityUser.fromUser(userByEmail);
    }
}
