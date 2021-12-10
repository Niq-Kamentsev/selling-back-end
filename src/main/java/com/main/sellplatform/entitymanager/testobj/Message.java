package com.main.sellplatform.entitymanager.testobj;

import com.main.sellplatform.entitymanager.GeneralObject;
import com.main.sellplatform.entitymanager.annotation.Attribute;
import com.main.sellplatform.entitymanager.annotation.Objtype;
import com.main.sellplatform.entitymanager.annotation.Reference;

import java.util.Date;

@Objtype(5)
public class Message extends GeneralObject {
    @Reference(attributeId = 28)
    User sender;
    @Reference(attributeId = 29)
    User receiver;
    @Attribute(attrTypeId = 31, type = Attribute.ValueType.DATE_VALUE)
    Date date;
    @Attribute(attrTypeId = 32)
    String msg;
}
