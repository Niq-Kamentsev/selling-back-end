package com.main.sellplatform.entitymanager;


import com.main.sellplatform.entitymanager.analyzer.DbConnector;
import com.main.sellplatform.entitymanager.analyzer.TableGetter;
import com.main.sellplatform.entitymanager.analyzer.TableSetter;
import com.main.sellplatform.entitymanager.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.*;
import java.util.Date;

@Component
public class EntityPresenter {
    DbConnector connector;
    JdbcTemplate jdbcTemplate;
    Class<?> clazz;

    @Autowired
    public EntityPresenter(DbConnector connector, JdbcTemplate jdbcTemplate) {
        this.connector = connector;
        this.jdbcTemplate = jdbcTemplate;
    }

    public Object[] get(Class<?> clazz, String where) {
        this.clazz = clazz;
        String sql = TableGetter.getSqlGet(clazz,where,null);

        Object[] objects = executeGet4(sql);
        return objects;

    }



    public ResultSet executeQuery(String query){
        return connector.executeGet(query);
    }

    public void insert(Object o) {
        /*connector.connect();
        String sql = TableAdder.getSqlInsert(o);
        System.out.println(sql);
        //connector.executeQuery(sql);
        connector.disconnect();
        */
    }



    public ResultSet executeGet2(String sql) {
        List<ResultSet> query = jdbcTemplate.query(sql, new RowMapper<ResultSet>() {
            @Override
            public ResultSet mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs;
            }
        });
        return query.get(0);

    }



    public Object[] executeGet3(String sql) {
        return jdbcTemplate.query(sql, new ResultSetExtractor<Object[]>() {
            @Override
            public Object[] extractData(ResultSet rs) throws SQLException, DataAccessException {
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
            }
        });
    };


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







}
