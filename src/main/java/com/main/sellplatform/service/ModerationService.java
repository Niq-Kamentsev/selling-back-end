package com.main.sellplatform.service;

import com.main.sellplatform.persistence.dao.LotModerationDao;
import com.main.sellplatform.persistence.dao.UserDao;
import com.main.sellplatform.persistence.entity.Lot;
import com.main.sellplatform.persistence.entity.ModeratingLot;
import com.main.sellplatform.persistence.entity.User;
import com.main.sellplatform.persistence.entity.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ModerationService {
    private final UserDao userDao;
    private final LotModerationDao lotModerationDao;

    @Autowired
    public ModerationService(UserDao userDao, LotModerationDao lotModerationDao) {
        this.userDao = userDao;
        this.lotModerationDao = lotModerationDao;
    }

    public List<Lot> getAllModeratingLots() {
        return lotModerationDao.getAllModeratingLots();
    }

    public Boolean publishLot(Lot lot, String moderator) {
        return lotModerationDao.publishLot(lot, moderator);
    }

    public Boolean rejectLot(Lot lot, String moderator) {
        return lotModerationDao.rejectLot(lot, moderator);
    }

    public List<Lot> getBannedLots() {
        return lotModerationDao.getBannedLots();
    }

    public Boolean banLot(Lot lot) {
        return lotModerationDao.banLot(lot);
    }

    public Boolean unbanLot(Lot lot) {
        return lotModerationDao.unbanLot(lot);
    }

    public List<Lot> findBannedLots(String keyword) {
        return lotModerationDao.findBannedLots(keyword);
    }

    public List<ModeratingLot> getModeratorHistory(String username) {
        return lotModerationDao.getModeratorHistory(username);
    }

    public Boolean updateModeratedLot(ModeratingLot lot, String username) {
        User user = userDao.getUserByEmail(username);
        if (user.getRole().equals(Role.MODER) && !Objects.equals(lot.getModerator().getId(), user.getId())) return null;
        return lotModerationDao.updateModeratedLot(lot, username);
    }

    public Boolean cancelModerationDecision(ModeratingLot lot, String username) {
        User user = userDao.getUserByEmail(username);
        if (user.getRole().equals(Role.MODER) && !Objects.equals(lot.getModerator().getId(), user.getId())) return null;
        return lotModerationDao.cancelModerationDecision(lot);
    }

    public List<ModeratingLot> getAllModerationHistory() {
        return lotModerationDao.getAllModerationHistory();
    }
}
