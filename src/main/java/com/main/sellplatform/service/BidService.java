package com.main.sellplatform.service;

import com.main.sellplatform.entitymanager.testobj.Bid;
import com.main.sellplatform.persistence.dao.BidDao;
import com.main.sellplatform.persistence.entity.Lot;
import com.main.sellplatform.persistence.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BidService {
    @Autowired
    BidDao bidDao;
    @Autowired
    UserService userService;
    @Autowired
    LotService lotService;

    public boolean makeBid(Bid bid, String userName){
        User user = userService.getUserByEmail(userName);
        bid.setUser(user);
        Long lotId = bid.getLot().getId();
        Lot lot = lotService.getLot(lotId);
        bid.setLot(lot);
        return bidDao.makeBid(bid);
    }

    public Bid getFinalBidByLot(Long lotId){
        return bidDao.getFinBidByLot(lotId);
    }
}
