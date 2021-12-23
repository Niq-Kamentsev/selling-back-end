package com.main.sellplatform.entitymanager;


import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.main.sellplatform.entitymanager.analyzer.DbConnector;
import com.main.sellplatform.entitymanager.analyzer.TableGetter;

import javax.sql.DataSource;
import javax.xml.crypto.Data;

@Component
public class    EntityPresenter {
    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public EntityPresenter( DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Object[] get(Class<?> clazz, String where, List<Object> statements) {

        String sql = TableGetter.getSqlGet(clazz,where,null);
        System.out.println(sql);
        return executeGet(clazz, sql, statements);
    }


    public Object[] executeGet(Class<?> clazz, String sql, List<Object> statements) {
        return jdbcTemplate.query(con -> {
            PreparedStatement st = con.prepareStatement(sql);
            if (statements != null)
                for (int i = 0; i < statements.size(); ++i) {
                    st.setObject(i + 1, statements.get(i));
                }
            return st;
        }, rs -> {
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
