package com.main.sellplatform.service;

import com.main.sellplatform.persistence.entity.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    public User getUser() {
        User user = new User();
        user.setId(1);
        user.setFirstName("Fist Name");
        user.setLastName("Last Name");
        return user;
    }
}
