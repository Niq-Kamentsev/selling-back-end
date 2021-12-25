package com.main.sellplatform.entitymanager.analyzer;


import com.main.sellplatform.entitymanager.GeneralObject;
import com.main.sellplatform.entitymanager.annotation.*;
import org.springframework.beans.PropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TableGetter {

    public static String getSqlGet(Class<?> clazz, String wher, Integer ref_id) {
        StringBuilder sql = new StringBuilder();

        List<Field> extensions = new ArrayList<>();
        List<Field> associations = new ArrayList<>();
        List<Field> attributes = new ArrayList<>();
        Integer objectType_id = clazz.getAnnotation(Objtype.class).value();

        getFields(clazz, extensions, associations, attributes);

        StringBuilder select = new StringBuilder();
        StringBuilder from = new StringBuilder();
        StringBuilder where = new StringBuilder();

        select.append(getSelect(objectType_id, attributes, ref_id));
        from.append(getFrom(objectType_id, attributes,ref_id));
        where.append("\nWHERE OBJ_").append(objectType_id).append(".object_type_id = ").append(objectType_id);

        for (Field field : extensions) {
            String foreign = getSqlGet(field.getType(), null,null);
            String[] foreigns = foreign.split("SELECT |FROM |WHERE");
            select.append(", ").append(foreigns[1]);
            String[] froms = foreigns[2].split("LEFT JOIN");
            from.append(" LEFT JOIN ").append(froms[0]).append(" ON OBJ_").append(objectType_id).append(".parent_id = OBJ_")
                    .append(field.getType().getAnnotation(Objtype.class).value()).append(".object_id");
            //.append(field.getAnnotation(Parent.class).objectId()).append(".object_id");
            for (int i = 1; i < froms.length; ++i) {
                from.append(" LEFT JOIN\t").append(froms[i]);
            }
            where.append(" AND ").append(foreigns[3]);
        }
        for (Field field : associations) {
            Integer curOT = field.getType().getAnnotation(Objtype.class).value();
            Integer curRT = field.getAnnotation(Reference.class).attributeId();
            String foreign = getSqlGet(field.getType(), null, curRT);
            String[] foreigns = foreign.split("SELECT |FROM |WHERE");
            String[] selects = foreigns[1].split(", ");
            //select.append(", ").append(foreigns[1]);
            for (String sel : selects) {
                sel = sel.replace(" ", "");
                sel = sel.replace("\n", "");
                sel = sel.substring(0, sel.length() - 1);
                select.append(", ").append(sel).append("_REF").append(field.getAnnotation(Reference.class).attributeId()).append("\"");
            }

			String[] froms = foreigns[2].split("LEFT JOIN");
			from.append("\nLEFT JOIN OBJREFERENCE OBJ_").append(curOT).append("_REF").append(curRT).append("_REF")
					.append(" ON OBJ_").append(objectType_id).append(ref_id == null ? "" : ("_REF" + ref_id))
					.append(".object_id = ").append("OBJ_").append(curOT).append("_REF").append(curRT)
					.append("_REF.object_id").append("\nLEFT JOIN OBJECTS OBJ_").append(curOT).append("_REF")
					.append(curRT).append(" ON OBJ_").append(curOT).append("_REF").append(curRT)
					.append("_REF.reference = ").append("OBJ_").append(curOT).append("_REF").append(curRT)
					.append(".object_id");
            for (int i = 1; i < froms.length; ++i) {
                from.append(" LEFT JOIN ").append(froms[i]);//.append("_REF").append(field.getAnnotation(Reference.class).attributeId()).append("");
            }

            where.append(" AND (OBJ_").append(curOT).append("_REF").append(curRT).append(".object_type_id = ").append(curOT)
                    .append(" OR OBJ_").append(curOT).append("_REF").append(curRT).append(".object_type_id IS NULL").append(")")
                    .append(" AND (OBJ_").append(curOT).append("_REF").append(curRT).append("_REF.attr_id = ").append(curRT)
                    .append(" OR OBJ_").append(curOT).append("_REF").append(curRT).append("_REF.attr_id IS NULL").append(")");
        }


        sql.append(select).append(from).append(where);

        if (wher != null) {
            String whSql = "SELECT * FROM (" + sql + ")";
            sql = new StringBuilder(whSql);
            sql.append("\n\t").append(wher);
        }

        return sql.toString();
    }

    private static String getSelect(Integer objectType_id, List<Field> attributes, Integer ref_id) {
        StringBuilder select = new StringBuilder();

        select.append("\nSELECT OBJ_").append(objectType_id).append(ref_id==null?"":("_REF"+ref_id)).append(".object_id \"OBJ_").append(objectType_id).append("ID\", ")
                .append("OBJ_").append(objectType_id).append(ref_id==null?"":("_REF"+ref_id)).append(".name \"OBJ_").append(objectType_id).append("NAME\", ")
                .append("OBJ_").append(objectType_id).append(ref_id==null?"":("_REF"+ref_id)).append(".description \"OBJ_").append(objectType_id).append("DESCR\", ");

        for (Field field : attributes) {
            if (field.isAnnotationPresent(Id.class)) continue;
            if (field.getAnnotation(Attribute.class).number()) {
                select.append("TO_NUMBER(");
            }
            select.append("OBJ_").append(objectType_id).append(ref_id==null?"":("_REF"+ref_id)).append("ATTR_").append(field.getAnnotation(Attribute.class).attrTypeId());
            if (field.getAnnotation(Attribute.class).type().equals(Attribute.ValueType.LIST)) {
                select.append("LIST.VALUE");
            } else {
                select.append(".").append(field.getAnnotation(Attribute.class).type());
            }
            if (field.getAnnotation(Attribute.class).number()) {
                select.append(")");
            }
            select.append(" \"OBJ_").append(objectType_id).append(ref_id==null?"":("_REF"+ref_id)).append("ATTR_").append(field.getAnnotation(Attribute.class).attrTypeId()).append("\", ");
        }

        return select.substring(0, select.length() - 2);
    }

    private static String getFrom(Integer objectType_id, List<Field> attributes, Integer ref_id) {
        StringBuilder from = new StringBuilder();

        from.append("\nFROM OBJECTS OBJ_").append(objectType_id).append(" LEFT JOIN ");

        for (Field field : attributes) {
            if (field.isAnnotationPresent(Id.class)) continue;
            from.append("\n\tATTRIBUTES ").append("OBJ_").append(objectType_id).append(ref_id==null?"":("_REF"+ref_id)).append("ATTR_").append(field.getAnnotation(Attribute.class).attrTypeId())
                    .append(" ON OBJ_").append(objectType_id).append(ref_id==null?"":("_REF"+ref_id)).append(".object_id = ")
                    .append("OBJ_").append(objectType_id).append(ref_id==null?"":("_REF"+ref_id))
                    .append("ATTR_").append(field.getAnnotation(Attribute.class).attrTypeId())
                    .append(".object_id AND ").append("OBJ_").append(objectType_id).append(ref_id==null?"":("_REF"+ref_id))
                    .append("ATTR_").append(field.getAnnotation(Attribute.class).attrTypeId())
                    .append(".attr_id = ").append(field.getAnnotation(Attribute.class).attrTypeId())
                    .append(" LEFT JOIN ");
            if (field.getAnnotation(Attribute.class).type().equals(Attribute.ValueType.LIST)) {
                from.append("\n\tLISTS ").append("OBJ_").append(objectType_id).append(ref_id==null?"":("_REF"+ref_id))
                        .append("ATTR_").append(field.getAnnotation(Attribute.class).attrTypeId()).append("LIST")
                        .append(" ON ").append("OBJ_").append(objectType_id).append(ref_id==null?"":("_REF"+ref_id))
                        .append("ATTR_").append(field.getAnnotation(Attribute.class).attrTypeId()).append("LIST.list_value_id = ")
                        .append("OBJ_").append(objectType_id).append(ref_id==null?"":("_REF"+ref_id))
                        .append("ATTR_").append(field.getAnnotation(Attribute.class).attrTypeId()).append(".list_value_id LEFT JOIN ");
            }
        }

        return from.substring(0, from.length() - "LEFT JOIN ".length());
    }

    public static void getFields(Class<?> clazz, List<Field> extensions, List<Field> associations, List<Field> attributes) {
        Field[] fieldList = clazz.getDeclaredFields();
        for (Field field : fieldList) {
            Annotation[] annotations = field.getAnnotations();
            for (Annotation annotation : annotations) {
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

    public static Object[] getObjects(ResultSet rs, Class<?> clazz) throws SQLException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if(rs==null)return null;
        ArrayList<Object> objects = new ArrayList<>();
        List<Field> extensions = new ArrayList<>();
        List<Field> associations = new ArrayList<>();
        List<Field> attributes = new ArrayList<>();
        getFields(clazz, extensions, associations, attributes);
        if (clazz.getSuperclass().isAssignableFrom(GeneralObject.class))
            getFields(clazz.getSuperclass(), extensions, associations, attributes);

        int objectType_id = clazz.getAnnotation(Objtype.class).value();
        ResultSetMetaData rsMetaData = rs.getMetaData();
        int rowCount = rsMetaData.getColumnCount();

        while (rs.next()) {
            Constructor<?> ctor = clazz.getConstructor();
            Object object = ctor.newInstance();
            for (int i = 0; i < rowCount; ++i) {
                String cName = rsMetaData.getColumnName(i + 1);
                for (Field attribute : attributes) {
                    if (attribute.isAnnotationPresent(Id.class)) {
                        if (cName.contains("OBJ_" + objectType_id) && cName.contains("ID")) {
                            runSetter(attribute.getName(), rs.getObject(cName), object);
                            break;
                        }
                    } else if (attribute.isAnnotationPresent(Attribute.class)) {
                        if (cName.contains("OBJ_" + objectType_id) && cName.contains("ATTR_" + attribute.getAnnotation(Attribute.class).attrTypeId())) {
                            runSetter(attribute.getName(), rs.getObject(cName), object);
                            break;
                        }
                    } else if (attribute.isAnnotationPresent(Description.class)) {
                        if (cName.contains("OBJ_" + objectType_id) && cName.contains("DESCR")) {
                            runSetter(attribute.getName(), rs.getObject(cName), object);
                            break;
                        }
                    } else if (attribute.isAnnotationPresent(Name.class)) {
                        if (cName.contains("OBJ_" + objectType_id) && cName.contains("NAME")) {
                            runSetter(attribute.getName(), rs.getObject(cName), object);
                            break;
                        }
                    }
                }
            }
            for (Field extension : extensions) {
                Object ext;
                ext = getObject(rs, rs.getRow(), extension, Parent.class);
                runSetter(extension.getName(), ext, object);
            }
            for (Field association : associations) {
                Object ass;
                ass = getObject(rs, rs.getRow(), association, Reference.class);
                runSetter(association.getName(), ass, object);
            }
            objects.add(object);
        }

        return objects.toArray();
    }

    public static Object getObject(ResultSet rs, int row, Field field, Class<?> type) throws SQLException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        //rs.absolute(row);
        Class<?> clazz = field.getType();

        Object object = clazz.getConstructor().newInstance();
        List<Field> extensions = new ArrayList<>();
        List<Field> associations = new ArrayList<>();
        List<Field> attributes = new ArrayList<>();
        getFields(clazz, extensions, associations, attributes);
        if (clazz.getSuperclass().isAssignableFrom(GeneralObject.class))
            getFields(clazz.getSuperclass(), extensions, associations, attributes);

        int objectType_id = clazz.getAnnotation(Objtype.class).value();
        Integer attr_id = null;

        if (type.isAssignableFrom(Reference.class)) {
            attr_id = field.getAnnotation(Reference.class).attributeId();
        }

        ResultSetMetaData rsMetaData = rs.getMetaData();
        int rowCount = rsMetaData.getColumnCount();

        for (int i = 0; i < rowCount; ++i) {
            String cName = rsMetaData.getColumnName(i + 1);
            for (Field attribute : attributes) {
                if (type.isAssignableFrom(Reference.class)) {
                    if (attribute.isAnnotationPresent(Id.class)) {
                        if (cName.contains("_REF" + attr_id) && cName.contains("OBJ_" + objectType_id)
                                && cName.contains("ID")) {
                            runSetter(attribute.getName(), rs.getObject(cName), object);
                            break;
                        }
                    } else if (attribute.isAnnotationPresent(Attribute.class)) {
                        if (cName.contains("_REF" + attr_id) && cName.contains("OBJ_" + objectType_id)
                                && cName.contains("ATTR_" + attribute.getAnnotation(Attribute.class).attrTypeId())) {
                            runSetter(attribute.getName(), rs.getObject(cName), object);
                            break;
                        }
                    } else if (attribute.isAnnotationPresent(Description.class)) {
                        if (cName.contains("_REF" + attr_id) && cName.contains("OBJ_" + objectType_id)
                                && cName.contains("NAME")) {
                            runSetter(attribute.getName(), rs.getObject(cName), object);
                            break;
                        }
                    } else if (attribute.isAnnotationPresent(Name.class)) {
                        if (cName.contains("_REF" + attr_id) && cName.contains("OBJ_" + objectType_id)
                                && cName.contains("DESCR")) {
                            runSetter(attribute.getName(), rs.getObject(cName), object);
                            break;
                        }
                    }
                } else {
                    if (attribute.isAnnotationPresent(Id.class)) {
                        if (cName.contains("OBJ_" + objectType_id) && cName.contains("ID")) {
                            runSetter(attribute.getName(), rs.getObject(cName), object);
                            break;
                        }
                    } else if (attribute.isAnnotationPresent(Attribute.class)) {
                        if (cName.contains("OBJ_" + objectType_id) && cName.contains("ATTR_" + attribute.getAnnotation(Attribute.class).attrTypeId())) {
                            runSetter(attribute.getName(), rs.getObject(cName), object);
                            break;
                        }
                    } else if (attribute.isAnnotationPresent(Description.class)) {
                        if (cName.contains("OBJ_" + objectType_id) && cName.contains("DESCR")) {
                            runSetter(attribute.getName(), rs.getObject(cName), object);
                            break;
                        }
                    } else if (attribute.isAnnotationPresent(Name.class)) {
                        if (cName.contains("OBJ_" + objectType_id) && cName.contains("NAME")) {
                            runSetter(attribute.getName(), rs.getObject(cName), object);
                            break;
                        }
                    }
                }
            }
        }

        return object;
    }

    public static void runSetter(String field, Object value, Object o) {
        PropertyAccessor myAccessor = PropertyAccessorFactory.forDirectFieldAccess(o);
        if (Objects.requireNonNull(myAccessor.getPropertyType(field)).isAssignableFrom(Float.class)
                || Objects.requireNonNull(myAccessor.getPropertyType(field)).isAssignableFrom(Double.class) && value!=null) {
            value = Double.parseDouble(value.toString().replace(",", "."));
        }
        myAccessor.setPropertyValue(field, value);
    }

}
