package com.main.sellplatform.entitymanager;


import com.main.sellplatform.entitymanager.annotation.Description;
import com.main.sellplatform.entitymanager.annotation.Id;
import com.main.sellplatform.entitymanager.annotation.Name;

public class GeneralObject {
    @Id
    protected Long id;
    @Name
    protected String name;
    @Description
    protected String descr;

    public GeneralObject(Long id, String name, String descr) {
        this.id = id;
        this.name = name;
        this.descr = descr;
    }

    public GeneralObject() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }
}
