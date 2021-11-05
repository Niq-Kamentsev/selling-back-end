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

    public User getUser(Long id){
        final User user = new User();
        try(PreparedStatement preparedStatement = connection.prepareStatement("select * from test_user where id = (?)")) {
            preparedStatement.setLong(1, id);
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
        try(PreparedStatement preparedStatement = connection.prepareStatement("insert into test_user(id,email,first_name,last_name,password,activation_code,role,is_active)  values (3,?,?,?,?,?,?,?)")) {
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getFirstName());
            preparedStatement.setString(3, user.getLastName());
            preparedStatement.setNString(4, user.getPassword());
            preparedStatement.setNString(5, user.getActivationCode());
            preparedStatement.setString( 6,Role.USER.name());
            preparedStatement.setString(7,setBoolean(user.isActive()));
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void deleteUser(Long id){
        try(PreparedStatement preparedStatement = connection.prepareStatement("delete test_user where id = (?)")) {
            preparedStatement.setLong(1, id);
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
                user.setActive(getBoolean(resultSet.getString("is_active")));
            }
            resultSet.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return user;
    }




    public User getUserByActivatedCode(String code){
        User user = new User();
        try(PreparedStatement preparedStatement = connection.prepareStatement("select * from test_user where activation_code = (?)")) {
            preparedStatement.setString(1, code);
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

    public void updateActiveUser(User user){
        try(PreparedStatement preparedStatement = connection.prepareStatement("update  test_user set is_active = ?, activation_code = ? where email = (?)")) {
            preparedStatement.setString(1, setBoolean(user.isActive()));
            preparedStatement.setNString(2 ,user.getActivationCode());
            preparedStatement.setNString(3, user.getEmail());
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void updatePasswordUser(User user){
        try(PreparedStatement preparedStatement = connection.prepareStatement("update  test_user set password = ? where email = (?)")) {
            preparedStatement.setNString(1, user.getPassword());
            preparedStatement.setNString(2, user.getEmail());
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }





    private boolean getBoolean(String b) {
        if (b.equals("Y"))
            return true;
        else return false;
    }
    private String setBoolean(boolean b){
        if (b)
            return "Y";
        else return "N";
    }

    private Role getRoleUser(String role){
        if ("ADMIN".equals(role)) {
            return Role.ADMIN;
        }
        return Role.USER;
    }

    public void updateEmailPassword(User user,String newEmail) {
        try(PreparedStatement preparedStatement = connection.prepareStatement("update  test_user set email = ? where id = (?)")) {
            preparedStatement.setNString(1, newEmail);
            preparedStatement.setLong(2, user.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
