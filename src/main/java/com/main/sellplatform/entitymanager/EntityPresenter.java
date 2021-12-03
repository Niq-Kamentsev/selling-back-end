package com.main.sellplatform.entitymanager;


import com.main.sellplatform.entitymanager.analyzer.DbConnector;
import com.main.sellplatform.entitymanager.analyzer.TableGetter;
import com.main.sellplatform.entitymanager.annotation.Objtype;
import com.main.sellplatform.entitymanager.annotation.Reference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class EntityPresenter {
    DbConnector connector;

    @Autowired
    public EntityPresenter(DbConnector connector) {
        this.connector = connector;
    }
    
	public List<Long> getDistinctReferences(Class<?> clazz, String where, Reference refId, Objtype objRefType) {
		String sql = "SELECT DISTINCT OBJ_" + objRefType.value() + "ID" + "_REF" + refId.attributeId() + " FROM ("
				+ TableGetter.getSqlGet(clazz, where, null) + ")";
		ResultSet rs = connector.executeGet(sql);
		List<Long> result = new ArrayList<>();
		try {
			String cName = rs.getMetaData().getColumnLabel(1);
			while(rs.next()) {
				result.add(rs.getLong(cName));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

    public Object[] get(Class<?> clazz, String where) {

        String sql = TableGetter.getSqlGet(clazz,where,null);
        System.out.println(sql);
        ResultSet rs = connector.executeGet(sql);
        try {
            Object[] objects = TableGetter.getObjects(rs,clazz);
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
}