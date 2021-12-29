package com.main.sellplatform.persistence.dao;

import com.main.sellplatform.entitymanager.EntityManager;
import com.main.sellplatform.entitymanager.analyzer.Queries;
import com.main.sellplatform.entitymanager.testobj.Bid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BidDao {
    private final EntityManager entityManager;
    private final Queries queries;

    @Autowired
    BidDao(EntityManager entityManager, Queries queries) {
        this.entityManager = entityManager;
        this.queries = queries;
    }

    public boolean makeBid(Bid bid) {
        return entityManager.merge(bid) != null;
    }

    public Bid getFinBidByLot(Long lot) {
        List<Object> statements = new ArrayList<>();
        statements.add(lot);
        return entityManager.getObjectByWhere(Bid.class, queries.whereByFinBid(), statements);
    }
    public Bid getBidById(Long id) {
        return this.getBidById(id, null);
    }

    public Bid getBidById(Long id, String where) {
        return (Bid) entityManager.getObjectById(Bid.class, id, where, null);
    }
}
