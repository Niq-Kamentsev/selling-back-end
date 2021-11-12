package com.main.sellplatform.persistence.dao;

import com.main.sellplatform.persistence.entity.Lot;
import com.main.sellplatform.persistence.entity.User;
import com.main.sellplatform.persistence.entity.enums.LotStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class LotDao {
    private final Connection connection;
    @Autowired
    private UserDao userDao;
    @Autowired
    private LotModerationDao lotModerationDao;

    @Autowired
    public LotDao(Connection connection) {
        this.connection = connection;
    }

    public Lot getLot(Long id) {
        Lot lot = new Lot();
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "select * from test_lot where id = ?"
        )) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                lot.setId(resultSet.getLong("id"));
                lot.setTitle(resultSet.getString("title"));
                lot.setTitle(resultSet.getString("description"));
                lot.setStatus(LotStatus.valueOf(resultSet.getString("status")));
                lot.setOwner(userDao.getUser(resultSet.getLong("user_id")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return lot;
    }

    public List<Lot> getUserLots(Long userId) {
        List<Lot> lotList = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "select * from test_lot where user_id = ? and status = 'PUBLISHED'"
        )) {
            preparedStatement.setLong(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Lot lot = new Lot();
                lot.setId(resultSet.getLong("id"));
                lot.setTitle(resultSet.getString("title"));
                lot.setTitle(resultSet.getString("description"));
                lot.setStatus(LotStatus.valueOf(resultSet.getString("status")));
                lot.setOwner(userDao.getUser(userId));
                lotList.add(lot);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return lotList;
    }

    public List<Lot> getMyLots(String username) {
        List<Lot> lotList = new ArrayList<>();
        User user = userDao.getUserByEmail(username);
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "select * from test_lot where user_id = ?"
        )) {
            preparedStatement.setLong(1, user.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Lot lot = new Lot();
                lot.setId(resultSet.getLong("id"));
                lot.setTitle(resultSet.getString("title"));
                lot.setTitle(resultSet.getString("description"));
                lot.setStatus(LotStatus.valueOf(resultSet.getString("status")));
                lot.setOwner(user);
                lotList.add(lot);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return lotList;
    }

    public List<Lot> getPublishedLots() {
        return getLotsByStatus("PUBLISHED");
    }

    public List<Lot> getLotsByStatus(String status) {
        List<Lot> lotList = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("select * from test_lot where status = '" + status + "'");
            while (resultSet.next()) {
                Lot lot = new Lot();
                lot.setId(resultSet.getLong("id"));
                lot.setTitle(resultSet.getString("title"));
                lot.setTitle(resultSet.getString("description"));
                lot.setStatus(LotStatus.valueOf(resultSet.getString("status")));
                lot.setOwner(userDao.getUser(resultSet.getLong("user_id")));
                lotList.add(lot);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return lotList;
    }

    public Boolean addLot(Lot lot, String username) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "insert into test_lot (title, user_id) values (?, ?)"
        )) {
            User user = userDao.getUserByEmail(username);
            preparedStatement.setString(1, lot.getTitle());
            preparedStatement.setLong(2, user.getId());
            preparedStatement.executeUpdate();
            lotModerationDao.addModeratingLot();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return true;
    }

    public Boolean updateLot(Lot lot) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "update test_lot set title = ?, description = ?, status = ?, user_id = ? where id = ?"
        )) {
            preparedStatement.setString(1, lot.getTitle());
            preparedStatement.setString(2, lot.getDescription());
            preparedStatement.setString(3, lot.getStatus().name());
            preparedStatement.setLong(4, lot.getOwner().getId());
            preparedStatement.setLong(5, lot.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return true;
    }

    public Boolean deleteLot(Lot lot) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("delete from test_lot where id = ?")) {
            preparedStatement.setLong(1, lot.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return true;
    }

    public List<Lot> findPublishedLots(String keyword) {
        return getLotsByKeyword(keyword, "PUBLISHED");
    }

    public List<Lot> getLotsByKeyword(String keyword, String status) {
        List<Lot> lotList = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(
                    "select * from test_lot where status = '" + status + "' and" +
                            "(title like '%" + keyword + "%' or description like '%" + keyword + "%')"
            );
            while (resultSet.next()){
                Lot lot = new Lot();
                lot.setId(resultSet.getLong("id"));
                lot.setTitle(resultSet.getString("title"));
                lot.setTitle(resultSet.getString("description"));
                lot.setStatus(LotStatus.valueOf(resultSet.getString("status")));
                lot.setOwner(userDao.getUser(resultSet.getLong("user_id")));
                lotList.add(lot);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return lotList;
    }

    public Boolean changeLotStatus(Lot lot, String status) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "update test_lot set status = ? where id = ?"
        )) {
            preparedStatement.setString(1, status);
            preparedStatement.setLong(2, lot.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return true;
    }
}
