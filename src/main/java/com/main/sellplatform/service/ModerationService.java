package com.main.sellplatform.service;

import com.main.sellplatform.entitymanager.testobj.User;
import com.main.sellplatform.persistence.dao.LotDao;
import com.main.sellplatform.persistence.dao.LotModerationDao;
import com.main.sellplatform.persistence.dao.UserDao;
import com.main.sellplatform.persistence.entity.Lot;
import com.main.sellplatform.persistence.entity.ModeratingLot;
import com.main.sellplatform.persistence.entity.enums.LotStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class ModerationService {
    private final UserDao userDao;
    private final LotDao lotDao;
    private final LotModerationDao lotModerationDao;

    @Autowired
    public ModerationService(UserDao userDao, LotDao lotDao, LotModerationDao lotModerationDao) {
        this.userDao = userDao;
        this.lotDao = lotDao;
        this.lotModerationDao = lotModerationDao;
    }

    public List<Lot> getAllModeratingLots() {
        return lotModerationDao.getAllModeratingLots();
    }

    public Boolean publishLot(Long lotId, String username) {
//        Lot lot = lotDao.getLot(lotId);
//        if (lot == null || lot.getEndDate() == null) return false;
//        if (lot.getEndDate().isBefore(LocalDateTime.now())) {
//            rejectLot(lotId, username, "The lot has expired");
//            return false;
//        } else if (!lot.getStatus().equals(LotStatus.PUBLISHED) ) {
//            lotModerationDao.publishLot(lot, username);
//            return true;
//        }
        return false;
    }

    public Boolean rejectLot(Long lotId, String username, String cause) {
        Lot lot = lotDao.getLot(lotId);
        if (lot == null) return false;
        return lotModerationDao.rejectLot(lot, username, cause);
    }

    public List<Lot> getBannedLots() {
        return lotModerationDao.getBannedLots();
    }

    public Boolean banLot(Long lotId) {
        Lot lot = lotDao.getLot(lotId);
        if (lot == null || lot.getStatus() == null || lot.getStatus().equals(LotStatus.BANNED)) {
            return false;
        }
        return lotModerationDao.banLot(lotId);
    }

    public Boolean unbanLot(Long lotId) {
        Lot lot = lotDao.getLot(lotId);
        if (lot == null || lot.getStatus() == null
                || lot.getStatus().equals(LotStatus.PUBLISHED) || lot.getStatus().equals(LotStatus.WAITING)) {
            return false;
        }
        return lotModerationDao.unbanLot(lotId);
    }

    public List<Lot> findBannedLots(String keyword) {
        return lotModerationDao.findBannedLots(keyword);
    }

    public List<ModeratingLot> getModeratorHistory(String username) {
        if (userDao.getUserByEmail(username) == null) return null;
        return lotModerationDao.getModeratorHistory(username);
    }

    public Boolean updateModeratedLot(Long moderLotId, String username) {
        if (!checkAccess(moderLotId, username)) return false;
        return lotModerationDao.updateModeratedLot(moderLotId, username);
    }

    public Boolean cancelModerationDecision(Long moderLotId, String username) {
        if (!checkAccess(moderLotId, username)) return false;
        return lotModerationDao.cancelModerationDecision(moderLotId);
    }

    public List<ModeratingLot> getAllModerationHistory() {
        return lotModerationDao.getAllModerationHistory();
    }

    private Boolean checkAccess(Long moderLotId, String username) {
        User user = userDao.getTestUserByEmail(username);
        if (user == null || lotModerationDao.getModeratingLot(moderLotId) == null
        || (!Objects.equals(lotModerationDao.getModeratingLot(moderLotId).getModerator().getId(), user.getId()))) {
            return false;
        }
        return true;
    }
}
