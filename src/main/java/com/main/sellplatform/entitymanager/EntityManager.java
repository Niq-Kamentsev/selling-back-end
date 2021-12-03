package com.main.sellplatform.entitymanager;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.main.sellplatform.entitymanager.annotation.Objtype;
import com.main.sellplatform.entitymanager.annotation.Reference;

@Component("entityManager")
public class EntityManager {
    EntityPresenter entityPresenter;

    @Autowired
    public EntityManager(EntityPresenter entityPresenter) {
        this.entityPresenter = entityPresenter;
    }
    
    public List<Long> getDistinctReferences(Class<? extends GeneralObject> clazz, String where, Reference refId, Objtype objRefType) {
    	return entityPresenter.getDistinctReferences(clazz, where, refId, objRefType);
    }
    
    public Object[] getObjects(Class<? extends GeneralObject> clazz, String where) {
    	Object[] objects = entityPresenter.get(clazz, where);
        if (objects == null) return null;
        for (int i = 0; i < objects.length; ++i) {
            objects[i] = clazz.cast(objects[i]);
        }
		return objects;
    }

    public Object getObjectById(Class<? extends GeneralObject> clazz, Integer id) {
        Object[] objects = entityPresenter.get(clazz, "OBJ_" + clazz.getAnnotation(Objtype.class).value() + "ID = " + id);
        if (objects == null || objects.length == 0) return null;
        Object object = objects[0];
        if (object == null) return null;

        return clazz.cast(object);
    }

    public Object[] getAllObjects(Class<? extends GeneralObject> clazz) {
        Object[] objects = entityPresenter.get(clazz, null);
        if (objects == null) return null;
        for (int i = 0; i < objects.length; ++i) {
            objects[i] = clazz.cast(objects[i]);
        }

        return objects;
    }

}
