package com.main.sellplatform.entitymanager.testobj;

import com.main.sellplatform.entitymanager.GeneralObject;
import com.main.sellplatform.entitymanager.annotation.Attribute;
import com.main.sellplatform.entitymanager.annotation.Objtype;
import com.main.sellplatform.entitymanager.annotation.Reference;
import com.main.sellplatform.persistence.entity.User;

import java.util.Date;

@Objtype(3)
public class Lot extends GeneralObject {

    @Reference(attributeId = 29 , fetch = Reference.FetchType.EAGER)
    User user;
    @Attribute(attrTypeId = 12)
    String nameLot;
    @Attribute(attrTypeId = 14, number = true)
    Double startPrice;
    @Attribute(attrTypeId = 15, number = true)
    Double minPrice;
    @Attribute(attrTypeId = 16, number = true)
    Double salePrice;
    @Attribute(attrTypeId = 17)
    String status;
    @Attribute(attrTypeId = 21, type = Attribute.ValueType.DATE_VALUE)
    Date startDate;
    @Attribute(attrTypeId = 22, type = Attribute.ValueType.DATE_VALUE)
    Date endDate;
    @Attribute(attrTypeId = 23, type = Attribute.ValueType.DATE_VALUE)
    Date creationDate;
    @Attribute(attrTypeId = 25)
    String imgPath;
    @Attribute(attrTypeId = 18)
    String category;
    @Attribute(attrTypeId = 20)
    String location;


    public Lot() {
    }

    public com.main.sellplatform.persistence.entity.User getUser() {
        return user;
    }

    public void setUser(com.main.sellplatform.persistence.entity.User user) {
        this.user = user;
    }

    public String getNameLot() {
        return nameLot;
    }

    public void setNameLot(String nameLot) {
        this.nameLot = nameLot;
    }

    public Double getStartPrice() {
        return startPrice;
    }

    public void setStartPrice(Double startPrice) {
        this.startPrice = startPrice;
    }

    public Double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }

    public Double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    @Override
    public String toString() {
        return "Lot{" +
                "id = "  + super.getId() +
                "user=" + user +
                ", name='" + nameLot + '\'' +
                ", startPrice=" + startPrice +
                ", minPrice=" + minPrice +
                ", salePrice=" + salePrice +
                ", status='" + status + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", creationDate=" + creationDate +
                ", imgPath='" + imgPath + '\'' +
                ", category='" + category + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
