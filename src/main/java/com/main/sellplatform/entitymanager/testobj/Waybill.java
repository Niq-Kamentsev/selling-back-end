package com.main.sellplatform.entitymanager.testobj;



import com.main.sellplatform.entitymanager.GeneralObject;
import com.main.sellplatform.entitymanager.annotation.Attribute;
import com.main.sellplatform.entitymanager.annotation.Objtype;

import java.util.Date;

@Objtype(value = 2)
public class Waybill extends GeneralObject {
    @Attribute(attrTypeId = 7, type = Attribute.ValueType.DATE_VALUE)
    Date rdate;
    @Attribute(attrTypeId = 8, number = true)
    Double price;
    @Attribute(attrTypeId = 9 )
    String address;


    public Waybill() {
    }

    public Waybill(Date rdate, Double price, String address) {
        this.rdate = rdate;
        this.price = price;
        this.address = address;
    }

    public Date getRdate() {
        return rdate;
    }

    public void setRdate(Date rdate) {
        this.rdate = rdate;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
