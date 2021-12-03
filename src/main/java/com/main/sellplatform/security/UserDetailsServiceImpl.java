package com.main.sellplatform.security;

import com.main.sellplatform.entitymanager.testdao.UserDao2;
import com.main.sellplatform.entitymanager.testobj.User;
import com.main.sellplatform.persistence.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserDao userDao;
    private final UserDao2 userDao2;
    @Autowired
    public UserDetailsServiceImpl(UserDao userDao, UserDao2 userDao2) {
        this.userDao = userDao;
        this.userDao2 = userDao2;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        //User userByEmail = userDao.getUserByEmail(email);
    	User userByEmail = userDao2.getUserByEmail(email);
        return SecurityUser.fromUser(userByEmail);
    }
}
