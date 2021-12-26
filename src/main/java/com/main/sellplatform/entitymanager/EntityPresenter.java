package com.main.sellplatform.entitymanager;


import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.main.sellplatform.entitymanager.analyzer.DbConnector2;
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
    DbConnector2 connector2;
    @Autowired
    public EntityPresenter( DataSource dataSource, DbConnector2 connector2) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.connector2 = connector2;
    }

    public Object[] get(Class<?> clazz, String where, List<Object> statements) {

        String sql = TableGetter.getSqlGet(clazz,where,null,0);
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

    public void delete(GeneralObject o) {
        if (o.getId() == null || o.getId() < 0) throw new IllegalArgumentException("Invalid object id");
        connector2.deleteObject(o.getId());
    }
}
