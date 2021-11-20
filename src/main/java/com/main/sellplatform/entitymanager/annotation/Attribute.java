package com.main.sellplatform.entitymanager.annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Attribute {
    int attrTypeId();
    ValueType type() default ValueType.VALUE;
    boolean number() default false;


    public enum ValueType{
        DATE_VALUE,
        VALUE,
        LIST
    }
}
