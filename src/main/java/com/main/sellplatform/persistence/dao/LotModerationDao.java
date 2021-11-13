package com.main.sellplatform.persistence.dao;

import com.main.sellplatform.persistence.entity.Lot;
import com.main.sellplatform.persistence.entity.ModeratingLot;
import com.main.sellplatform.persistence.entity.User;
import com.main.sellplatform.persistence.entity.enums.LotStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class LotModerationDao {
    private final Connection connection;
    @Autowired
    private UserDao userDao;
    @Autowired
    private LotDao lotDao;

    @Autowired
    public LotModerationDao(Connection connection) {
        this.connection = connection;
    }

    public ModeratingLot getModeratingLot(Long moderLotId) {
        ModeratingLot lot = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "select * from moder_lot where id = ?"
        )) {
            preparedStatement.setLong(1, moderLotId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                lot = new ModeratingLot();
                lot.setId(resultSet.getLong("id"));
                lot.setModerator(userDao.getUser(resultSet.getLong("moderator_id")));
                lot.setLot(lotDao.getLot(resultSet.getLong("lot_id")));
                lot.setDateTime(resultSet.getTimestamp("publish_date").toLocalDateTime());
                lot.setCause(resultSet.getString("cause"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return lot;
    }

    public List<Lot> getAllModeratingLots() {
        return lotDao.getLotsByStatus("WAITING");
    }

    public Boolean publishLot(Lot lot, String username) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "update moder_lot set moderator_id = ?, publish_date = ?, cause = ? where lot_id = ?"
        )) {
            User user = userDao.getUserByEmail(username);
            preparedStatement.setLong(1, user.getId());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatement.setString(3, "");
            preparedStatement.setLong(4, lot.getId());
            preparedStatement.executeUpdate();
            lotDao.changeLotStatus(lot.getId(), "PUBLISHED");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return true;
    }

    public Boolean rejectLot(Lot lot, String username, String cause) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "update moder_lot set moderator_id = ?, publish_date = ?, cause = ? where lot_id = ?"
        )) {
            User user = userDao.getUserByEmail(username);
            preparedStatement.setLong(1, user.getId());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatement.setString(3, cause);
            preparedStatement.setLong(4, lot.getId());
            preparedStatement.executeUpdate();
            lotDao.changeLotStatus(lot.getId(), "BANNED");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return true;
    }

    public Boolean addModeratingLot() {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "insert into moder_lot (lot_id) values (LOT_SEQUENCE.currval)"
        )) {
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return true;
    }

    public List<Lot> getBannedLots() {
        return lotDao.getLotsByStatus("BANNED");
    }

    public Boolean banLot(Long id) {
        return lotDao.changeLotStatus(id, "BANNED");
    }

    public Boolean unbanLot(Long id) {
        return lotDao.changeLotStatus(id, "PUBLISHED");
    }

    public List<Lot> findBannedLots(String keyword) {
        return lotDao.getLotsByKeyword(keyword, "BANNED");
    }

    public List<ModeratingLot> getModeratorHistory(String username) {
        List<ModeratingLot> moderLots = new ArrayList<>();
        User user = userDao.getUserByEmail(username);
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "select * from moder_lot where moderator_id = ? order by publish_date"
        )) {
            preparedStatement.setLong(1, user.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                ModeratingLot lot = new ModeratingLot();
                lot.setId(resultSet.getLong("id"));
                lot.setModerator(user);
                lot.setLot(lotDao.getLot(resultSet.getLong("lot_id")));
                lot.setDateTime(resultSet.getTimestamp("publish_date").toLocalDateTime());
                lot.setCause(resultSet.getString("cause"));
                moderLots.add(lot);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return moderLots;
    }

    public Boolean updateModeratedLot(Long moderLotId, String username) {
        Lot lot = getModeratingLot(moderLotId).getLot();
        if (lot.getStatus().equals(LotStatus.PUBLISHED)) {
            rejectLot(lot, username, ("Lot has been rejected by the decision of the "
                    + getModeratingLot(moderLotId).getModerator().getRole().name()));
            return true;
        } else if (lot.getStatus().equals(LotStatus.BANNED)) {
            publishLot(lot, username);
            return true;
        }
        return null;
    }

    public Boolean cancelModerationDecision(Long moderLotId) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "update moder_lot set moderator_id = ?, publish_date = ?, cause = ? where lot_id = ?"
        )) {
            Lot lot = getModeratingLot(moderLotId).getLot();
            preparedStatement.setNull(1, Types.INTEGER);
            preparedStatement.setNull(2, Types.TIMESTAMP);
            preparedStatement.setNull(3, Types.VARCHAR);
            preparedStatement.setLong(4, lot.getId());
            preparedStatement.executeUpdate();
            lotDao.changeLotStatus(lot.getId(), "WAITING");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return true;
    }

    public List<ModeratingLot> getAllModerationHistory() {
        List<ModeratingLot> moderLots = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("select * from moder_lot order by publish_date");
            while (resultSet.next()) {
                ModeratingLot lot = new ModeratingLot();
                lot.setId(resultSet.getLong("id"));
                lot.setModerator(userDao.getUser(resultSet.getLong("moderator_id")));
                lot.setLot(lotDao.getLot(resultSet.getLong("lot_id")));
                lot.setDateTime(resultSet.getTimestamp("publish_date").toLocalDateTime());
                lot.setCause(resultSet.getString("cause"));
                moderLots.add(lot);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return moderLots;
    }
}
