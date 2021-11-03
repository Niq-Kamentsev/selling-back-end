package com.main.sellplatform.persistence.dao;

import com.main.sellplatform.persistence.entity.User;
import com.main.sellplatform.persistence.entity.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserDao {
    private final Connection connection;

    @Autowired
    public UserDao(Connection connection) {
        this.connection = connection;
    }

    public List<User> getUsers(){
        List<User> userList = new ArrayList<>();
        try(Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("select * from test_user");
            while (resultSet.next()){
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setFirstName(resultSet.getString("first_name"));
                user.setLastName(resultSet.getString("last_name"));
                user.setRole(getRoleUser(resultSet.getString("role")));
                userList.add(user);
            }
            resultSet.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return userList;
    }

    public User getUser(Integer id){
        final User user = new User();
        try(PreparedStatement preparedStatement = connection.prepareStatement("select * from test_user where id = (?)")) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                user.setId(resultSet.getLong("id"));
                user.setEmail(resultSet.getNString("email"));
                user.setFirstName(resultSet.getString("first_name"));
                user.setLastName(resultSet.getString("last_name"));
                user.setPassword(resultSet.getNString("password"));
                user.setRole(getRoleUser(resultSet.getString("role")));
            }
            resultSet.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return user;
    }

    public void saveUser(User user){
        try(PreparedStatement preparedStatement = connection.prepareStatement("insert into test_user(id,email,first_name,last_name,password,role)  values (3,?,?,?,?,?)")) {
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getFirstName());
            preparedStatement.setString(3, user.getLastName());
            preparedStatement.setNString(4, user.getPassword());
            preparedStatement.setString( 5,Role.USER.name());
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void deleteUser(Integer id){
        try(PreparedStatement preparedStatement = connection.prepareStatement("delete test_user where id = (?)")) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public User getUserByEmail(String email){
        User user = new User();
        try(PreparedStatement preparedStatement = connection.prepareStatement("select * from test_user where email = (?)")) {
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                user.setId(resultSet.getLong("id"));
                user.setEmail(resultSet.getNString("email"));
                user.setFirstName(resultSet.getString("first_name"));
                user.setLastName(resultSet.getString("last_name"));
                user.setPassword(resultSet.getNString("password"));
                user.setRole(getRoleUser(resultSet.getString("role")));
            }
            resultSet.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return user;
    }

    private Role getRoleUser(String role){
        if ("ADMIN".equals(role)) {
            return Role.ADMIN;
        }
        return Role.USER;
    }
}
