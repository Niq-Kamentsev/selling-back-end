package com.main.sellplatform.service;

import com.main.sellplatform.persistence.dao.LotDao;
import com.main.sellplatform.persistence.dao.UserDao;
import com.main.sellplatform.persistence.entity.Lot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public List<Lot> getUserLots(Long userId) {
        return lotDao.getUserLots(userId);
    }

    public List<Lot> getMyLots(String username) {
        return lotDao.getMyLots(username);
    }

    public List<Lot> getAllLots() {
        return lotDao.getPublishedLots();
    }

    public Lot getLot(Long id){
        Lot lot = lotDao.getLot(id);
        if (Objects.isNull(lot.getId())) return null;
        return lot;
    }

    public Boolean addLot(Lot lot, String username) {
        return lotDao.addLot(lot, username);
    }

    public Boolean updateLot(Lot lot, String username) {
        if (!Objects.equals(lot.getOwner().getId(), userDao.getUserByEmail(username).getId())) return null;
        return lotDao.updateLot(lot);
    }

    public Boolean deleteLot(Lot lot, String username) {
        if (!Objects.equals(lot.getOwner().getId(), userDao.getUserByEmail(username).getId())) return null;
        return lotDao.deleteLot(lot);
    }

    public List<Lot> findLots(String keyword) {
        return lotDao.findPublishedLots(keyword);
    }
}
