package com.main.sellplatform.entitymanager.testdao;

import com.google.common.collect.Lists;
import com.main.sellplatform.entitymanager.EntityManager;
import com.main.sellplatform.entitymanager.analyzer.Queries;


import com.main.sellplatform.persistence.entity.Lot;
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

    @Autowired
    public LotDao2(EntityManager entityManager, Queries queries) {
        this.entityManager = entityManager;
        this.queries = queries;
    }

    public List<Lot> getAllLots() {
        Object[] allObjects = entityManager.getAllObjects(Lot.class);
        Lot[] lots = Arrays.stream(allObjects).toArray(Lot[]::new);
        return Lists.newArrayList(lots);
    }

    public Lot[] getAllLots(String where, List<Object> statements) {
        System.out.println(queries.whereMessageMessages());
        Object[] objects = entityManager.getAllObjects(Lot.class, where, statements);
        if (objects == null) return null;
        Lot[] lots = new Lot[objects.length];
        for (int i = 0; i < objects.length; ++i) {
            lots[i] = (Lot) objects[i];
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
        }
        return lots;
    }

    public Lot getLotById(Long lotId, String where){
        return (Lot) entityManager.getObjectById(Lot.class,lotId,where,null);
    }



    public Lot saveLot(Lot lot){
        return entityManager.merge(lot);
    }

}
