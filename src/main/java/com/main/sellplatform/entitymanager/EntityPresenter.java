package com.main.sellplatform.entitymanager;


import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.main.sellplatform.entitymanager.analyzer.DbConnector2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.main.sellplatform.entitymanager.analyzer.DbConnector;
import com.main.sellplatform.entitymanager.analyzer.TableGetter;

@Component
public class EntityPresenter {
    DbConnector connector;
    DbConnector2 connector2;
    JdbcTemplate jdbcTemplate;
    Class<?> clazz;

    @Autowired
    public EntityPresenter(DbConnector connector, JdbcTemplate jdbcTemplate, DbConnector2 connector2) {
        this.connector = connector;
        this.jdbcTemplate = jdbcTemplate;
        this.connector2 = connector2;
    }

    public Object[] get(Class<?> clazz, String where, List<Object> statements) {

        String sql = TableGetter.getSqlGet(clazz, where, null);
        //System.out.println(sql);
        ResultSet rs = connector.executeGet(sql, statements);
        try {
            Object[] objects = TableGetter.getObjects(rs, clazz);
            rs.close();
            return objects;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Object[] get(Class<?> clazz, String where) {
        this.clazz = clazz;
        String sql = TableGetter.getSqlGet(clazz, where, null);

        Object[] objects = executeGet4(sql);
        return objects;

    }

    public ResultSet executeQuery(String query, List<Object> statements) {
        return connector.executeGet(query, statements);
    }

    public void insert(Object o) {
        /*connector.connect();
        String sql = TableAdder.getSqlInsert(o);
        System.out.println(sql);
        //connector.executeQuery(sql);
        connector.disconnect();
        */
    }

    public Object[] executeGet4(String sql) {
        return jdbcTemplate.query(sql, rs -> {
            try {
                Object[] objects = TableGetter.getObjects(rs, clazz);
                return objects;
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    public void delete(GeneralObject o) {
        if (o.getId() == null || o.getId() < 0) throw new IllegalArgumentException("Invalid object id");
        connector2.deleteObject(o.getId());
    }
}
