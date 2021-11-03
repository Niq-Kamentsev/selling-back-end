package com.main.sellplatform.persistence.dao;

import com.main.sellplatform.persistence.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserJdbcTemplateDao {
    private JdbcTemplate jdbcTemplate;
    @Autowired
    public UserJdbcTemplateDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<User> getUsers(){
        return jdbcTemplate.query("select * from userproject", ((resultSet , rowNum) -> {
            final User user = new User();
            user.setId(resultSet.getLong("id"));
            user.setFirstName(resultSet.getString("first_name"));
            return user;
        }));
    }
}
