package com.main.sellplatform.service;

import com.main.sellplatform.entitymanager.testdao.LotDao2;
import com.main.sellplatform.persistence.dao.LotDao;
import com.main.sellplatform.persistence.dao.UserDao;
import com.main.sellplatform.persistence.entity.Lot;
import com.main.sellplatform.persistence.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class LotService {
    private final UserDao userDao;

    private final LotDao lotDao;
    private final LotDao2 lotDao2;

    @Autowired
    public LotService(UserDao userDao, LotDao lotDao, LotDao2 lotDao2) {
        this.userDao = userDao;
        this.lotDao = lotDao;
        this.lotDao2 = lotDao2;
    }

    private String getLotSortCol(String col) {
        if(col==null)return null;
        switch (col) {
            case "Id":
                return "OBJ_3ATTR_10";
            case "Name":
                return "OBJ_3ATTR_12";
            case "Status":
                return "OBJ_3ATTR_17";
            case "Category":
                return "OBJ_3ATTR_18";
            case "StartDate":
                return "OBJ_3ATTR_21";
            case "EndDate":
                return "OBJ_3ATTR_22";
            case "StartPrice":
                return "OBJ_3ATTR_14";
            case "EndPrice":
                return "OBJ_3ATTR_15";
        }
        return null;
    }

    public List<com.main.sellplatform.entitymanager.testobj.Lot> getUserLots(String username, String sortCol) {
        User user = userDao.getUserByEmail(username);
        if (user == null) return null;
        return Arrays.asList(lotDao2.getUsersLots(user.getId(), getLotSortCol(sortCol)));
    }

    public List<com.main.sellplatform.entitymanager.testobj.Lot> getMyLots(String username, String sortCol) {
        User user = userDao.getUserByEmail(username);
        if (user == null) return null;
        return Arrays.asList(lotDao2.getUsersLots(user.getId(), getLotSortCol(sortCol)));
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

    public List<com.main.sellplatform.entitymanager.testobj.Lot> getBuyableLots() {
        List<com.main.sellplatform.entitymanager.testobj.Lot> res = Arrays.asList(
                lotDao2.getAllLots("OBJ_3ATTR_17 = 'NO BIDS' OR OBJ_3ATTR_17 = 'BIDDING'",null)
        );
        for(com.main.sellplatform.entitymanager.testobj.Lot lot:res){
            lot.setUser(null);
        }
        return res;
    }

    public com.main.sellplatform.entitymanager.testobj.Lot getBuyableLot(Long id) {
        com.main.sellplatform.entitymanager.testobj.Lot res = lotDao2.getLotById(id, "(OBJ_3ATTR_17 = 'NO BIDS' OR OBJ_3ATTR_17 = 'BIDDING')");
        if(res!=null) res.setUser(null);
        return res;
    }
}
