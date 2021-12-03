package com.main.sellplatform.entitymanager;


import com.main.sellplatform.entitymanager.annotation.Objtype;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("entityManager")
public class EntityManager {
    EntityPresenter entityPresenter;

    @Autowired
    public EntityManager(EntityPresenter entityPresenter) {
        this.entityPresenter = entityPresenter;
    }

    public Object getObjectById(Class<? extends GeneralObject> clazz, Long id, String where) {
        Object[] objects = entityPresenter.get(clazz, "WHERE "+where+" AND OBJ_" + clazz.getAnnotation(Objtype.class).value() + "ID = " + id);
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
    public Object[] getAllObjects(Class<? extends GeneralObject> clazz, String where) {
        Object[] objects = entityPresenter.get(clazz, "WHERE "+where);
        if (objects == null) return null;
        for (int i = 0; i < objects.length; ++i) {
            objects[i] = clazz.cast(objects[i]);
        }

        return objects;
    }

}
