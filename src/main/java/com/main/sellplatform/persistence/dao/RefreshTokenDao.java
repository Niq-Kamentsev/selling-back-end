package com.main.sellplatform.persistence.dao;

import com.main.sellplatform.persistence.entity.RefreshToken;
import com.main.sellplatform.persistence.entity.User;
import com.main.sellplatform.persistence.entity.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;

@Component
public class RefreshTokenDao {
    private final Connection connection;
    private final UserDao userDao;

    @Autowired
    public RefreshTokenDao(Connection connection, UserDao userDao) {
        this.connection = connection;
        this.userDao = userDao;
    }

    public RefreshToken getTokenById(Long id){
        final RefreshToken refreshToken = new RefreshToken();
        try(PreparedStatement preparedStatement = connection.prepareStatement("select * from refresh_token where id = (?)")) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                refreshToken.setId(resultSet.getLong("id"));
                refreshToken.setUser(userDao.getUser(resultSet.getLong("user_id")));
                refreshToken.setToken(resultSet.getNString("token"));
                refreshToken.setExpiryDate(resultSet.getObject("expiry_date", LocalDate.class));
            }
            resultSet.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return refreshToken;
    }


    public RefreshToken getTokenByToken(String token){
        final RefreshToken refreshToken = new RefreshToken();
        try(PreparedStatement preparedStatement = connection.prepareStatement("select * from refresh_token where token = (?)")) {
            preparedStatement.setNString(1, token);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                refreshToken.setId(resultSet.getLong("id"));
                refreshToken.setUser(userDao.getUser(resultSet.getLong("user_id")));
                refreshToken.setToken(resultSet.getNString("token"));
                refreshToken.setExpiryDate(resultSet.getObject("expiry_date", LocalDate.class));
            }
            resultSet.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return refreshToken;
    }

    public void saveToken(RefreshToken token){
        try(PreparedStatement preparedStatement = connection.prepareStatement("insert into refresh_token(user_id, token,expiry_date)  values (?,?,?)")) {
            preparedStatement.setLong(1, token.getUser().getId());
            preparedStatement.setNString(2,token.getToken());
            preparedStatement.setObject(3, token.getExpiryDate());
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
