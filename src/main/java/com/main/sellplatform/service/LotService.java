package com.main.sellplatform.service;

import com.main.sellplatform.persistence.dao.LotDao;
import com.main.sellplatform.persistence.dao.UserDao;
import com.main.sellplatform.persistence.entity.Lot;
import com.main.sellplatform.persistence.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class LotService {
    private final UserDao userDao;
    private final LotDao lotDao;

    @Autowired
    public LotService(UserDao userDao, LotDao lotDao) {
        this.userDao = userDao;
        this.lotDao = lotDao;
    }

    public List<Lot> getUserLots(String username) {
        User user = userDao.getUserByEmail(username);
        if (user == null) return null;
        return lotDao.getUserLots(user.getId());
    }

    public List<Lot> getMyLots(String username) {
        User user = userDao.getUserByEmail(username);
        if (user == null) return null;
        return lotDao.getMyLots(username);
    }

    public List<Lot> getAllLots() {
        return lotDao.getPublishedLots();
    }

    public Boolean addLot(Lot lot, String username) {
        if (!validateLot(lot, username)) return false;
        return lotDao.addLot(lot, username);
    }

    public Boolean updateLot(Lot lot, String username) {
        if (!validateLot(lot, username)) return false;
        return lotDao.updateLot(lot);
    }

    public Boolean deleteLot(Long id, String username) {
        if (lotDao.getLot(id).getOwner() == null || !Objects.equals(lotDao.getLot(id).getOwner().getId(), userDao.getUserByEmail(username).getId())) {
            return false;
        }
        return lotDao.deleteLot(id);
    }

    public List<Lot> findLots(String keyword) {
        return lotDao.findPublishedLots(keyword);
    }

    private Boolean validateLot(Lot lot, String username) {
        return Objects.equals(lot.getOwner().getId(), userDao.getUserByEmail(username).getId())
                && lot.getStartDate() != null
                && !lot.getStartDate().isBefore(LocalDateTime.now())
                && !lot.getStartDate().isAfter(lot.getEndDate())
                && !lot.getTerm().isAfter(lot.getEndDate())
                && !lot.getTerm().isBefore(lot.getStartDate());
    }
}
