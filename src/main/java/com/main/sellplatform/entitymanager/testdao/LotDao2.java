package com.main.sellplatform.entitymanager.testdao;

import com.google.common.collect.Lists;
import com.main.sellplatform.entitymanager.EntityManager;

import com.main.sellplatform.entitymanager.testobj.User;
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

    @Autowired
    public LotDao2(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<Lot> getAllLots() {
        Object[] allObjects = entityManager.getAllObjects(Lot.class);
        Lot[] lots = Arrays.stream(allObjects).toArray(Lot[]::new);
        return Lists.newArrayList(lots);
    }

    public Lot[] getUsersLots(Long userId, String sortCol) {
        List<Object> statements = new ArrayList<>();
        statements.add(userId);
        if(sortCol!=null)
          statements.add(sortCol);
        Object[] objects = entityManager.getAllObjects(Lot.class, "OBJ_1ID_REF29 = ?" +
                (sortCol == null ? "" : "\nORDER BY ?"),statements);
        if(objects==null)return null;
        Lot[] lots = new Lot[objects.length];
        for (int i = 0; i < objects.length; ++i) {
            lots[i] = (Lot) objects[i];
        }
        return lots;
    }

    public Lot getLotById(Long lotId, String where){
        return (Lot) entityManager.getObjectById(Lot.class,lotId,where,null);
    }

    public Object[] test() {
        try {
            return entityManager.getObjectsByIdSeq(User.class,"SELECT OBJECT_ID AS Id FROM objects WHERE OBJECT_TYPE_ID = 1",null);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public Lot saveLot(Lot lot){
        return entityManager.merge(lot);
    }

}
