package com.main.sellplatform.persistence.dao;

import com.main.sellplatform.persistence.entity.Lot;
import com.main.sellplatform.persistence.entity.User;
import com.main.sellplatform.persistence.entity.enums.LotStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class LotDao {
    private final Connection connection;
    @Autowired
    private UserDao userDao;

    @Autowired
    public LotDao(Connection connection) {
        this.connection = connection;
    }

    public Lot getLot(Long id) {
        Lot lot = new Lot();
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "select * from test_lot where id = ? and status = 'PUBLISHED'"
        )) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                lot.setId(resultSet.getLong("id"));
                lot.setTitle(resultSet.getString("title"));
                lot.setStatus(getStatus(resultSet.getString("status")));
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
                lot.setStatus(getStatus(resultSet.getString("status")));
                lot.setOwner(userDao.getUser(userId));
                lotList.add(lot);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return lotList;
    }

    public List<Lot> getAllLots() {
        List<Lot> lotList = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("select * from test_lot where status = 'PUBLISHED'");
            while (resultSet.next()) {
                Lot lot = new Lot();
                lot.setId(resultSet.getLong("id"));
                lot.setTitle(resultSet.getString("title"));
                lot.setStatus(getStatus(resultSet.getString("status")));
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
            addModeratingLot(lot);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return true;
    }

    public Boolean updateLot(Lot lot) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "update test_lot set title = ?, status = ?, user_id = ? where id = ?"
        )) {
            preparedStatement.setString(1, lot.getTitle());
            preparedStatement.setString(2, lot.getStatus().name());
            preparedStatement.setLong(3, lot.getOwner().getId());
            preparedStatement.setLong(4, lot.getId());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return true;
    }

    public Boolean deleteLot(Long id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("delete from test_lot where id = ?")) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return true;
    }

    public Boolean addModeratingLot(Lot lot) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "insert into moder_lot (lot_id) values (?)"
        )) {
            preparedStatement.setLong(1, lot.getId());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return true;
    }

    public List<Lot> getAllModeratingLots() {
        List<Lot> lotList = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("select * from test_lot where status = 'WAITING'");
            while (resultSet.next()){
                Lot lot = new Lot();
                lot.setId(resultSet.getLong("id"));
                lot.setTitle(resultSet.getString("title"));
                lot.setStatus(getStatus(resultSet.getString("status")));
                lot.setOwner(userDao.getUser(resultSet.getLong("user_id")));
                lotList.add(lot);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return lotList;
    }

    public Boolean publishLot(Lot lot, String moderator) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "update moder_lot set moderator_id = ?, publish_date = ? where lot_id = ?"
        )) {
            User user = userDao.getUserByEmail(moderator);
            preparedStatement.setLong(1, user.getId());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatement.setLong(3, lot.getId());
            changeLotStatus(lot, "'PUBLISHED'");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return true;
    }

    public Boolean rejectLot(Lot lot, String moderator) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "update moder_lot set moderator_id = ?, publish_date = ?, cause = ? where lot_id = ?"
        )) {
            User user = userDao.getUserByEmail(moderator);
            preparedStatement.setLong(1, user.getId());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatement.setString(3, "");
            preparedStatement.setLong(4, lot.getId());
            changeLotStatus(lot, "'REJECTED'");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return true;
    }

    private void changeLotStatus(Lot lot, String status) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "update test_lot set status = ? where id = ?"
        )) {
            preparedStatement.setString(1, status);
            preparedStatement.setLong(2, lot.getId());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private LotStatus getStatus(String status) {
        if (status.equals("PUBLISHED")) {
            return LotStatus.PUBLISHED;
        } else if (status.equals("WAITING")) {
            return LotStatus.WAITING;
        }
        return LotStatus.REJECTED;
    }
}
