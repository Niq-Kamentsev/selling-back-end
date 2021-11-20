package com.main.sellplatform.entitymanager.testobj;

import com.main.sellplatform.entitymanager.GeneralObject;
import com.main.sellplatform.entitymanager.annotation.Attribute;
import com.main.sellplatform.entitymanager.annotation.Objtype;
import com.main.sellplatform.entitymanager.annotation.Reference;

@Objtype(5)
public class Message extends GeneralObject {
    @Reference(attributeId = 28)
    User sender;
    @Reference(attributeId = 29)
    User receiver;
    @Attribute(attrTypeId = 30)
    Integer id;
    @Attribute(attrTypeId = 31)
    String date;
    @Attribute(attrTypeId = 32)
    String msg;
}
