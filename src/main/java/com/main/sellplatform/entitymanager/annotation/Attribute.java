package com.main.sellplatform.entitymanager.annotation;
import javax.validation.constraints.NotNull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Attribute {
    @NotNull
    int attrTypeId();
    @NotNull
    ValueType type() default ValueType.VALUE;
    @NotNull
    boolean number() default false;


    public enum ValueType{
        DATE_VALUE,
        VALUE,
        LIST
    }
}
