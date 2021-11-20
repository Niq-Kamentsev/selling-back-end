package com.main.sellplatform.entitymanager.testobj;

import com.main.sellplatform.entitymanager.GeneralObject;
import com.main.sellplatform.entitymanager.annotation.Attribute;
import com.main.sellplatform.entitymanager.annotation.Objtype;

@Objtype(1)
public class User extends GeneralObject {
    @Attribute(attrTypeId = 1)
    String email;
    @Attribute(attrTypeId = 2)
    String password;
}
