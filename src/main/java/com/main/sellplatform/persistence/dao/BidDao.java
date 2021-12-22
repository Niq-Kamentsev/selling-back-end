package com.main.sellplatform.persistence.dao;

import com.main.sellplatform.entitymanager.EntityManager;
import com.main.sellplatform.entitymanager.testobj.Bid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BidDao {
    private final EntityManager entityManager;

    @Autowired
    BidDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public boolean makeBid(Bid bid){
        return entityManager.merge(bid)!=null;
    }
}
