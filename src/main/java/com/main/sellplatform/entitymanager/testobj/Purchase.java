package com.main.sellplatform.entitymanager.testobj;


import com.main.sellplatform.entitymanager.GeneralObject;
import com.main.sellplatform.entitymanager.annotation.Attribute;
import com.main.sellplatform.entitymanager.annotation.Id;
import com.main.sellplatform.entitymanager.annotation.Objtype;
import com.main.sellplatform.entitymanager.annotation.Parent;

@Objtype(value = 3)
public class Purchase extends GeneralObject {

    @Attribute(attrTypeId = 10)
    String clientCard;
    @Parent()
    Waybill waybill;

    public Purchase() {
    }

    public Purchase(String clientCard, Waybill waybill) {
        this.clientCard = clientCard;
        this.waybill = waybill;
    }

    public String getClientCard() {
        return clientCard;
    }

    public void setClientCard(String clientCard) {
        this.clientCard = clientCard;
    }

    public Waybill getWaybill() {
        return waybill;
    }

    public void setWaybill(Waybill waybill) {
        this.waybill = waybill;
    }

    @Override
    public String toString() {
        return "Purchase{" +
                "clientCard='" + clientCard + '\'' +
                ", waybill=" + waybill +
                ", id=" + id +
                '}';
    }
}
