package com.main.sellplatform.entitymanager.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Reference {
    int attributeId();
    FetchType fetch() default FetchType.LAZY;


    public enum FetchType{
        LAZY,
        EAGER
    }
}
