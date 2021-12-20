package com.main.sellplatform.entitymanager;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.main.sellplatform.entitymanager.analyzer.TableSetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.main.sellplatform.entitymanager.annotation.Objtype;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import javax.xml.crypto.Data;

@Component("entityManager")
public class EntityManager {
    EntityPresenter entityPresenter;
    TableSetter tableSetter;
    JdbcTemplate jdbcTemplate;

    @Autowired
    public EntityManager(EntityPresenter entityPresenter, TableSetter tableSetter, DataSource dataSource) {
        this.entityPresenter = entityPresenter;
        this.tableSetter = tableSetter;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public<T extends GeneralObject> T getObjectById(Class<T> clazz, Long id, String where, List<Object> statements) {
        Object[] objects = entityPresenter.get(clazz,
                "WHERE " + (where == null ? "" : where + " AND ") + "OBJ_" + clazz.getAnnotation(Objtype.class).value() + "ID = " + id,
                statements);
        if (objects == null || objects.length == 0) return null;
        Object object = objects[0];
        if (object == null) return null;

        return (T) object;
    }


    public<T extends GeneralObject> T getObjectByWhere(Class<T> clazz, String where, List<Object> statements) {
        Object[] objects = entityPresenter.get(clazz,
                "WHERE " + (where == null ? "" : where ) ,
                statements);
        if (objects == null || objects.length == 0) return null;
        Object object = objects[0];
        if (object == null) return null;

        return (T) object;
    }


    public Object[] getObjectsByIdSeq(Class<? extends GeneralObject> clazz, String seq, List<Object> statements) throws SQLException {
        List<Object> objects = new ArrayList<>();
        jdbcTemplate.query(con -> {
            PreparedStatement st = con.prepareStatement(seq);
            if (statements != null)
                for (int i = 0; i < statements.size(); ++i) {
                    st.setObject(i + 1, statements.get(i));
                }
            return st;
        }, rs -> {
            Object obj = getObjectById(clazz, rs.getLong("Id"), null, null);
            if(!Objects.isNull(obj)){
                objects.add(obj);
            }
        });
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
        Object[] objects = entityPresenter.get(clazz, (where == null ? "" : "WHERE "+where), statements);
        if (objects == null) return null;
        for (int i = 0; i < objects.length; ++i) {
            objects[i] = clazz.cast(objects[i]);
        }

        return objects;
    }


    public <T extends GeneralObject> T merge(T entity){
        try {
            return tableSetter.getSqlInsertQuery(entity);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

}