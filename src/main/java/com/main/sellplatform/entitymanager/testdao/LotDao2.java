package com.main.sellplatform.entitymanager.testdao;

import com.google.common.collect.Lists;
import com.main.sellplatform.entitymanager.EntityManager;
import com.main.sellplatform.entitymanager.analyzer.Queries;


import com.main.sellplatform.entitymanager.testobj.Bid;
import com.main.sellplatform.persistence.dao.BidDao;
import com.main.sellplatform.persistence.entity.Lot;
import com.main.sellplatform.persistence.entity.enums.LotStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class LotDao2 {
    private final EntityManager entityManager;
    private final Queries queries;
    private final BidDao bidDao;

    @Autowired
    public LotDao2(EntityManager entityManager, Queries queries, BidDao bidDao) {
        this.entityManager = entityManager;
        this.queries = queries;
        this.bidDao = bidDao;
    }

    public List<Lot> getAllLots() {
        Object[] allObjects = entityManager.getAllObjects(Lot.class);
        Lot[] lots = new Lot[allObjects.length];
        for (int i = 0; i < allObjects.length; ++i) {
            lots[i] = (Lot) allObjects[i];
            getCurPrice(lots[i]);
        }
        return Lists.newArrayList(lots);
    }

    public List<Lot> getAllLots(String where, List<Object> statements) {
        System.out.println(queries.whereMessageMessages());
        Object[] objects = entityManager.getAllObjects(Lot.class, where, statements);
        if (objects == null) return null;
        List<Lot> lots = new ArrayList<>();
        for (Object object : objects) {
            lots.add((Lot) object);
            getCurPrice((Lot) object);
        }
        return lots;
    }
    
	public List<Lot> getPageOfLots(String where, List<Object> statements, Long offset, Long nextRowsCount) {
		Object[] objects = entityManager.getPageOfObjects(Lot.class, where, statements, offset, nextRowsCount);
		if (objects == null)
			return null;
		List<Lot> lots = new ArrayList<>();
		for (Object object : objects) {
			lots.add((Lot) object);
			getCurPrice((Lot) object);
		}
		return lots;
	}


    public Lot[] getUsersLots(Long userId, String sortCol) {
        List<Object> statements = new ArrayList<>();
        statements.add(userId);
        if (sortCol != null)
            statements.add(sortCol);
        Object[] objects = entityManager.getAllObjects(Lot.class, queries.whereByLotOwnerId() +
                (sortCol == null ? "" : queries.orderBy()), statements);
        if (objects == null) return null;
        Lot[] lots = new Lot[objects.length];
        for (int i = 0; i < objects.length; ++i) {
            lots[i] = (Lot) objects[i];
            getCurPrice(lots[i]);
        }
        return lots;
    }

    public Lot getLotById(Long lotId, String where) {
        Lot res = (Lot) entityManager.getObjectById(Lot.class, lotId, where, null);
        getCurPrice(res);
        return res;
    }


    public Lot saveLot(Lot lot) {
        return entityManager.merge(lot);
    }

    private void getCurPrice(Lot lot) {
        if (lot == null) return;
        LotStatus status = lot.getStatus();
        if (status == LotStatus.NO_BIDS) {
            lot.countCurrentPrice();
        } else {
            Bid bid = bidDao.getLastBidOfLot(lot.getId());
            if (bid == null) lot.countCurrentPrice();
            else lot.setCurrentPrice(bid.getPrice());
        }
    }
}
