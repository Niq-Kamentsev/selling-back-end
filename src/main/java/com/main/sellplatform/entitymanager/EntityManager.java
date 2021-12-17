package com.main.sellplatform.entitymanager;


import com.main.sellplatform.entitymanager.analyzer.TableSetter;
import com.main.sellplatform.entitymanager.annotation.Objtype;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component("entityManager")
public class EntityManager {
    EntityPresenter entityPresenter;
    TableSetter tableSetter;

    @Autowired
    public EntityManager(EntityPresenter entityPresenter, TableSetter tableSetter) {
        this.entityPresenter = entityPresenter;
        this.tableSetter = tableSetter;

    }

    public Object getObjectById(Class<? extends GeneralObject> clazz, Long id, String where) {
        Object[] objects = entityPresenter.get(clazz, "WHERE "+(where==null?"":where+" AND ")+"OBJ_" + clazz.getAnnotation(Objtype.class).value() + "ID = " + id);
        if (objects == null || objects.length == 0) return null;
        Object object = objects[0];
        if (object == null) return null;

        return clazz.cast(object);
    }

    public Object[] getObjectsByIdSeq(Class<? extends GeneralObject> clazz, String seq) throws SQLException {
        ResultSet rs = entityPresenter.executeQuery(seq);

        List<Object> objects = new ArrayList<>();

        while(rs.next()){
            Object obj = getObjectById(clazz,rs.getLong("Id"),null);
            if(obj!=null){
                objects.add(obj);
            }
        }
        return objects.toArray();
    }

    public Object[] getAllObjects(Class<? extends GeneralObject> clazz) {
        Object[] objects = entityPresenter.get(clazz, null);
        if (objects == null) return null;
        for (int i = 0; i < objects.length; ++i) {
            objects[i] = clazz.cast(objects[i]);
        }

        return objects;
    }
    public Object[] getAllObjects(Class<? extends GeneralObject> clazz, String where) {
        Object[] objects = entityPresenter.get(clazz, "WHERE "+where);
        if (objects == null) return null;
        for (int i = 0; i < objects.length; ++i) {
            objects[i] = clazz.cast(objects[i]);
        }

        return objects;
    }

    @Transactional
    public <T extends GeneralObject> T merge(T entity) throws NoSuchFieldException, IllegalAccessException {

        return tableSetter.getSqlInsertQuery(entity);


    }




}
