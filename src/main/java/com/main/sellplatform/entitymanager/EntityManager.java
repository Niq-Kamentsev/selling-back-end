package com.main.sellplatform.entitymanager;


import javax.validation.constraints.NotNull;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.main.sellplatform.entitymanager.annotation.Objtype;

@Component("entityManager")
public class EntityManager {
    EntityPresenter entityPresenter;

    @Autowired
    public EntityManager(EntityPresenter entityPresenter) {
        this.entityPresenter = entityPresenter;
    }

    public Object getObjectById(Class<? extends GeneralObject> clazz, @NotNull Long id, String where, List<Object> statements) {
        Object[] objects = entityPresenter.get(clazz,
                "WHERE " + (where == null ? "" : where + " AND ") + "OBJ_" + clazz.getAnnotation(Objtype.class).value() + "ID = " + id,
                statements);
        if (objects == null || objects.length == 0) return null;
        Object object = objects[0];
        if (object == null) return null;

        return clazz.cast(object);
    }

    public Object[] getObjectsByIdSeq(Class<? extends GeneralObject> clazz, String seq, List<Object> statements) throws SQLException {
        ResultSet rs = entityPresenter.executeQuery(seq, statements);

        List<Object> objects = new ArrayList<>();

        while (rs.next()) {
            Object obj = getObjectById(clazz, rs.getLong("Id"), null, null);
            if (obj != null) {
                objects.add(obj);
            }
        }
        rs.close();
        return objects.toArray();
    }

    public Object[] getAllObjects(Class<? extends GeneralObject> clazz) {
        Object[] objects = entityPresenter.get(clazz, null, null);
        if (objects == null) return null;
        for (int i = 0; i < objects.length; ++i) {
            objects[i] = clazz.cast(objects[i]);
        }

        return objects;
    }

    public Object[] getAllObjects(Class<? extends GeneralObject> clazz, String where, List<Object> statements) {
        Object[] objects = entityPresenter.get(clazz, (where == null ? "" : "WHERE " + where), statements);
        if (objects == null) return null;
        for (int i = 0; i < objects.length; ++i) {
            objects[i] = clazz.cast(objects[i]);
        }

        return objects;
    }

}