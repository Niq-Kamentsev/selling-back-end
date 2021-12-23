package com.main.sellplatform.entitymanager.analyzer;

import com.main.sellplatform.entitymanager.EntityManager;
import com.main.sellplatform.entitymanager.EntityPresenter;
import com.main.sellplatform.entitymanager.GeneralObject;
import com.main.sellplatform.entitymanager.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.Types;
import java.util.*;

@Component()
public class TableSetter {
    private final DbConnector2 dbConnector;
    private final EntityPresenter entityPresenter;
    Set<Integer> setObjTypeId = new HashSet<>();

    Map<Field,Integer> attributesValues = new HashMap<>();

    @Autowired
    public TableSetter(DbConnector2 dbConnector, EntityPresenter entityPresenter) {
        this.dbConnector = dbConnector;
        this.entityPresenter = entityPresenter;
    }



    @Transactional
    public <T extends GeneralObject> T  getSqlInsertQuery(T clazz ) throws IllegalAccessException, NoSuchFieldException {
//        if (setObjTypeId.contains(clazz.getClass().getAnnotation(Objtype.class).value())){
//            return null;
//        }
        setObjTypeId.add(clazz.getClass().getAnnotation(Objtype.class).value());
        Object id = isUpdate(clazz);
        
        List<Field> extensions = new ArrayList<>();
        List<Field> associations = new ArrayList<>();
        List<Field> attributes = new ArrayList<>();
        getFields(clazz, attributes, extensions, associations);
        if(!Objects.isNull(id)){
            getUpdateObjects(extensions, clazz,  id);
            if(!associations.isEmpty()){
                updateObjReference(clazz, associations, id);
            }
            updateAttributes(clazz, attributes, id);
            return (T) getObjectById(clazz.getClass(),(Long) id, null);
        }
        Long objectId = getInsertIntoObjects(extensions, clazz);
        if(!associations.isEmpty()){
            getInsertIntoObjReference(clazz,associations, objectId );

        }
        getInsertIntoAttributes(clazz,attributes, objectId);
        return (T) getObjectById(clazz.getClass(), objectId, null);

    }


    private<T extends GeneralObject> Long getInsertIntoObjects(List<Field> extensions, T clazz) throws NoSuchFieldException, IllegalAccessException {
        List<Object> values = new ArrayList<>();


        StringBuilder insertIntoObjects = new StringBuilder();

        insertIntoObjects.append("\ninsert into objects (parent_id, object_type_id, name, description) values(");
        if (extensions.isEmpty()){
            insertIntoObjects.delete(22,33);
        }
        else {
            for (Field field :extensions) {
                field.setAccessible(true);
                Annotation[] annotations = field.getAnnotations();
                for (Annotation annotation : annotations){
                    if(annotation instanceof Parent){
                        Object o = field.get(clazz);
                        if (Objects.isNull(o)){
                            insertIntoObjects.delete(22,33);
                            break;
                        }
                        Long idObjUpdate = getIdObjUpdate(field, o);
                        T sqlInsertQuery = getSqlInsertQuery((T) o);
                        insertIntoObjects.append("?,");
                        values.add(sqlInsertQuery.getId());
                    }
                }
            }
        }
        values.add(clazz.getClass().getAnnotation(Objtype.class).value());
        insertIntoObjects.append("?").append(",");
        values.add(getNameObj(clazz));
        insertIntoObjects.append("?");
        values.add(getDescObj(clazz));
        insertIntoObjects.append(",? )");


        Long objectId = dbConnector.saveObjects(insertIntoObjects.toString(), values, "OBJECT_ID");
        setId(clazz, objectId);
        return objectId;
    }



    private<T extends GeneralObject> void getUpdateObjects(List<Field> extensions, T clazz, Object id) throws IllegalAccessException, NoSuchFieldException {
        List<Object> values = new ArrayList<>();
        StringBuilder updateObjects = new StringBuilder();
        updateObjects.append("merge into OBJECTS target\n" +
                "using ( select ? as parent_id,  ? as object_id , ? as object_type_id, ? as name , ? as description from dual) source\n" +
                "on ( target.OBJECT_ID = source.object_id)\n" +
                "when matched then\n" +
                "    update set target.PARENT_ID = source.parent_id, target.OBJECT_TYPE_ID = source.object_type_id , target.NAME = source.name, target.DESCRIPTION = source.description\n" +
                "when not matched then\n" +
                "    insert (PARENT_ID, OBJECT_TYPE_ID, NAME, DESCRIPTION)\n" +
                "    values (source.parent_id, source.object_type_id, source.name , source.description)");
        if (extensions.isEmpty()){
            System.out.println(updateObjects);
            updateObjects.delete(404,414);
            updateObjects.delete(452,469);
            values.add(null);
            System.out.println(updateObjects);
        }
        else {
            for (Field field :extensions) {
                field.setAccessible(true);
                Annotation[] annotations = field.getAnnotations();
                for (Annotation annotation1 : annotations){
                    if(annotation1 instanceof Parent){
                        Object o = field.get(clazz);
                        if (Objects.isNull(o)){
                            values.add(null);
                            continue;
                        }
                        T sqlInsertQuery = getSqlInsertQuery((T) o);
                        values.add(sqlInsertQuery.getId());

                    }
                }

            }
        }
        values.add(id);
        values.add(clazz.getClass().getAnnotation(Objtype.class).value());
        values.add(getNameObj(clazz));
        values.add(getDescObj(clazz));
        dbConnector.updateObject(updateObjects.toString(), values);

    }



    private void getInsertIntoAttributes(Object obj, List<Field> attributes, Long objectId) throws IllegalAccessException {
        for (Field attribute: attributes){
            StringBuilder insertIntoAttributes = new StringBuilder();
            List<Object> value = new ArrayList<>();
            attribute.setAccessible(true);
            if(Objects.isNull(attribute.get(obj))){
                continue;
            }
            insertIntoAttributes.append("insert into attributes (ATTR_ID, OBJECT_ID, VALUE) values(");

            Attribute annotation = attribute.getAnnotation(Attribute.class);
            if(annotation != null){
                if(annotation.type().equals( Attribute.ValueType.LIST)){
                    insertIntoAttributes.replace(43,50,  ", LIST_VALUE_ID");
                }
                if(annotation.type().equals(Attribute.ValueType.DATE_VALUE)){
                    insertIntoAttributes.replace(43,49 ,"DATE_VALUE");
                }
            }
            value.add(attributesValues.get(attribute));
            value.add(objectId);
            insertIntoAttributes.append("?,?,?)");
            value.add(attribute.get(obj));
            dbConnector.
                    saveObjectsNotId(insertIntoAttributes.toString(), value);
        }


    }


    private void updateAttributes(Object obj , List<Field> attributes, Object objectId) throws IllegalAccessException {
        for (Field attribute: attributes){
            StringBuilder updateAttributes = new StringBuilder();
            List<Object> value = new ArrayList<>();
            attribute.setAccessible(true);
            if(Objects.isNull(attribute.get(obj))){
                updateAttributes.append("delete from attributes where OBJECT_ID = ? and ATTR_ID = ?");
                value.add(objectId);
                value.add(attributesValues.get(attribute));
                dbConnector.updateObject(updateAttributes.toString(), value);
                continue;
            }
            updateAttributes.append("merge into ATTRIBUTES target using ( select ? as VALUE, ? as ATTR_ID , ? as object_id from dual) source on ( target.OBJECT_ID = source.object_id and target.ATTR_ID = source.ATTR_ID) when matched then update set  target.VALUE = source.VALUE where target.OBJECT_ID = source.object_id and target.ATTR_ID = source.ATTR_ID when not matched then insert (ATTR_ID, OBJECT_ID, VALUE) values ( source.attr_id, source.object_id , source.VALUE)");

            Attribute annotation = attribute.getAnnotation(Attribute.class);
            if(annotation != null){
                if(annotation.type().equals( Attribute.ValueType.LIST)){
                    updateAttributes.replace(183,188,  "LIST_VALUE_ID")
                            .replace(337,342,  "LIST_VALUE_ID");
                }
                if(annotation.type().equals(Attribute.ValueType.DATE_VALUE)){
                    updateAttributes.replace(219,224 ,"DATE_VALUE")
                            .replace(373,378,  "DATE_VALUE");
                }
            }
            value.add(attribute.get(obj));
            value.add(attributesValues.get(attribute));
            value.add(objectId);
            dbConnector.
                    updateObject(updateAttributes.toString(), value);
        }

    }


    private <T extends GeneralObject> void getInsertIntoObjReference(Object object,List<Field> associations, Long objectId) throws IllegalAccessException, NoSuchFieldException {
        List<Object> values = new ArrayList<>();
        StringBuilder insertIntoObjReference = new StringBuilder();
        for (Field association:associations){

            association.setAccessible(true);
            T o = (T) association.get(object);
            if(Objects.isNull(o)){
                break;
            }
            insertIntoObjReference.append("insert into objreference (ATTR_ID,OBJECT_ID,REFERENCE) values (");
            Reference annotationReference = association.getAnnotation(Reference.class);
            values.add(annotationReference.attributeId());
            values.add(objectId);
            insertIntoObjReference.append("?,?");
            Long idObj = getIdObj(association, o, insertIntoObjReference);
            if (dbConnector.getId(idObj)){
                values.add(idObj);
                getSqlInsertQuery(o);
                dbConnector.saveObjectsNotId(insertIntoObjReference.toString(), values);
            }
            else {
                getSqlInsertQuery(o);
                insertIntoObjReference.delete(66,73);
                values.add(getIdObj(association, o, insertIntoObjReference));
                dbConnector.saveObjectsNotId(insertIntoObjReference.toString(), values);
            }

        }


    }


    private<T extends GeneralObject> void updateObjReference(Object object,List<Field> associations, Object objectId) throws IllegalAccessException, NoSuchFieldException {
        List<Object> values = new ArrayList<>();
        StringBuilder updateObjReference = new StringBuilder();
        for (Field association:associations){
            association.setAccessible(true);
            T o = (T) association.get(object);
            if(Objects.isNull(o)){
                updateObjReference.append("delete from objreference where OBJECT_ID = ?");
                dbConnector.deleteObjectFromObjReference(updateObjReference.toString(), objectId);
                continue;
            }
            updateObjReference.append("merge into OBJREFERENCE target\n" +
                    "using ( select ? as attr_id, ? as reference , ? as object_id from dual) source\n" +
                    "on ( target.OBJECT_ID = source.object_id)\n" +
                    "when matched then\n" +
                    "    update set target.ATTR_ID = ATTR_ID,  target.REFERENCE = source.reference where target.OBJECT_ID = source.object_id\n" +
                    "when not matched then\n" +
                    "    insert (ATTR_ID, REFERENCE, OBJECT_ID)\n" +
                    "    values ( source.attr_id, source.reference, source.object_id )");
            Reference annotationReference = association.getAnnotation(Reference.class);
            if(annotationReference.fetch().equals(Reference.FetchType.LAZY)){
                return;
            }
            values.add(annotationReference.attributeId());
            Long idObj = getIdObjUpdate(association, o);
            if (dbConnector.getId(idObj)){
                values.add(idObj);
                values.add(objectId);
                getSqlInsertQuery(o);
                dbConnector.updateObject(updateObjReference.toString(), values);
            }
            else {
                getSqlInsertQuery(o);
                values.add(getIdObjUpdate(association, o));
                values.add(objectId);
                dbConnector.updateObject(updateObjReference.toString(), values);
            }

        }

    }



    private void getFields(Object object, List<Field> attributes, List<Field> extensions,  List<Field> associations) {
        Field[] fieldList = object.getClass().getDeclaredFields();
        for (Field field : fieldList) {
            Annotation[] annotations = field.getAnnotations();
            for (Annotation annotation : annotations) {
                if(annotation instanceof Attribute){
                    attributesValues.put(field, ((Attribute) annotation).attrTypeId());
                }
                if (annotation instanceof Attribute || annotation instanceof Id || annotation instanceof Description
                        || annotation instanceof Name) {
                    attributes.add(field);
                } else if (annotation instanceof Parent) {
                    extensions.add(field);
                } else if (annotation instanceof Reference) {
                    associations.add(field);
                }
            }
        }
    }




    private Long getIdObj(Field objRefer, Object o, StringBuilder insertIntoObjReference) throws IllegalAccessException {
        Field[] declaredFields = objRefer.getType().getSuperclass().getDeclaredFields();
        for (Field field : declaredFields){
            field.setAccessible(true);
            Annotation[] annotations = field.getAnnotations();
            for (Annotation annotation : annotations){
                if(annotation instanceof Id){
                    insertIntoObjReference.append(", ?)");
                    return (Long) field.get(o);
                }
            }
        }
        return null;

    }


    private String getDescObj( Object o) throws IllegalAccessException {
        Field[] declaredFields = o.getClass().getSuperclass().getDeclaredFields();
        for (Field field : declaredFields){
            field.setAccessible(true);
            Annotation[] annotations = field.getAnnotations();
            for (Annotation annotation : annotations){
                if(annotation instanceof Description){
                    return (String) field.get(o);
                }
            }
        }
        return null;
    }

    private String getNameObj(Object o) throws IllegalAccessException {
        Field[] declaredFields = o.getClass().getSuperclass().getDeclaredFields();
        for (Field field : declaredFields){
            field.setAccessible(true);
            Annotation[] annotations = field.getAnnotations();
            for (Annotation annotation : annotations){
                if(annotation instanceof Name){
                    return (String) field.get(o);
                }
            }
        }
        return null;
    }

    private Long getIdObjUpdate(Field objRefer, Object o) throws IllegalAccessException {
        Field[] declaredFields = objRefer.getType().getSuperclass().getDeclaredFields();
        for (Field field : declaredFields){
            field.setAccessible(true);
            Annotation[] annotations = field.getAnnotations();
            for (Annotation annotation : annotations){
                if(annotation instanceof Id){
                    return (Long) field.get(o);
                }
            }
        }
        return null;

    }



    private void setId(Object obj , Long objectId) throws NoSuchFieldException, IllegalAccessException {
        Field id = obj.getClass().getSuperclass().getDeclaredField("id");
        id.setAccessible(true);
        id.set(obj, objectId);

    }

    private Object isUpdate(Object object) throws IllegalAccessException, NoSuchFieldException {
        Field field = object.getClass().getSuperclass().getDeclaredField("id");
        field.setAccessible(true);
        return field.get(object);

    }



    public Object getObjectById(Class<? extends GeneralObject> clazz, Long id, String where) {
    	List<Object> statements = new ArrayList<>();
    	statements.add(id);
        Object[] objects = entityPresenter.get(clazz, "WHERE "+(where==null?"":where+" AND ")+"OBJ_" + clazz.getAnnotation(Objtype.class).value() + "ID = ?",statements);
        if (objects == null || objects.length == 0) return null;
        Object object = objects[0];
        if (object == null) return null;

        return clazz.cast(object);
    }



}
