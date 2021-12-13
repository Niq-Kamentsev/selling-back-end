package com.main.sellplatform.entitymanager;


import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.main.sellplatform.entitymanager.analyzer.DbConnector;
import com.main.sellplatform.entitymanager.analyzer.TableGetter;

@Component
public class EntityPresenter {
    DbConnector connector;

    @Autowired
    public EntityPresenter(DbConnector connector) {
        this.connector = connector;
    }

    public Object[] get(Class<?> clazz, String where, List<Object> statements) {

        String sql = TableGetter.getSqlGet(clazz,where,null);
        System.out.println(sql);
        ResultSet rs = connector.executeGet(sql,statements);
        try {
            Object[] objects = TableGetter.getObjects(rs,clazz);
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

    public ResultSet executeQuery(String query, List<Object> statements){
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
}
