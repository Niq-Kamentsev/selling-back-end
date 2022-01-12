package com.main.sellplatform.entitymanager.testobj;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.main.sellplatform.entitymanager.GeneralObject;
import com.main.sellplatform.entitymanager.annotation.Attribute;
import com.main.sellplatform.entitymanager.annotation.Objtype;
import com.main.sellplatform.entitymanager.annotation.Reference;
import com.main.sellplatform.persistence.entity.Lot;
import com.main.sellplatform.persistence.entity.User;
import com.main.sellplatform.persistence.entity.enums.BidStatus;

import java.util.Date;

@Objtype(6)
public class Bid extends GeneralObject {
    @Reference(attributeId = 33)
    Lot lot;
    @Reference(attributeId = 35)
    User user;
    @Attribute(attrTypeId = 34, type = Attribute.ValueType.DATE_VALUE)
    @JsonFormat(pattern="dd.MM.yyyy hh:mm")
    Date date;
    @Attribute(attrTypeId = 36)
    BidStatus status;
    @Attribute(attrTypeId = 38)
    Double price;

    public Lot getLot() {
        return lot;
    }

    public void setLot(Lot lot) {
        this.lot = lot;
    }

    public com.main.sellplatform.persistence.entity.User getUser() {
        return user;
    }

    public void setUser(com.main.sellplatform.persistence.entity.User user) {
        this.user = user;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BidStatus getStatus() {
        return status;
    }

    public void setStatus(BidStatus status) {
        this.status = status;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Bid{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", descr='" + descr + '\'' +
                ", lot=" + lot +
                ", user=" + user +
                ", date=" + date +
                ", status='" + status + '\'' +
                ", price=" + price +
                '}';
    }
}
