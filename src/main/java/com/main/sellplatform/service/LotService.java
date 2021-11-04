package com.main.sellplatform.service;

import com.main.sellplatform.persistence.dao.LotDao;
import com.main.sellplatform.persistence.entity.Lot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class LotService {
    private final LotDao lotDao;

    @Autowired
    public LotService(LotDao lotDao) {
        this.lotDao = lotDao;
    }

    public List<Lot> getUserLots(Long userId) {
        return lotDao.getUserLots(userId);
    }

    public List<Lot> getAllLots() {
        return lotDao.getAllLots();
    }

    public Lot getLot(Long id){
        Lot lot = lotDao.getLot(id);
        if (Objects.isNull(lot.getId())) return null;
        return lot;
    }

    public Boolean addLot(Lot lot, String username) {
        return lotDao.addLot(lot, username);
    }

    public Boolean updateLot(Lot lot) {
        return lotDao.updateLot(lot);
    }

    public Boolean deleteLot(Long id) {
        return lotDao.deleteLot(id);
    }

    public List<Lot> getAllModeratingLots() {
        return lotDao.getAllModeratingLots();
    }

    public Boolean publishLot(Lot lot, String moderator) {
        return lotDao.publishLot(lot, moderator);
    }

    public Boolean rejectLot(Lot lot, String moderator) {
        return lotDao.rejectLot(lot, moderator);
    }
}
