package com.main.sellplatform.persistence.entity;

import com.main.sellplatform.entitymanager.GeneralObject;
import com.main.sellplatform.entitymanager.annotation.Attribute;
import com.main.sellplatform.entitymanager.annotation.Objtype;
import com.main.sellplatform.entitymanager.annotation.Reference;
import com.main.sellplatform.persistence.entity.enums.LotStatus;
import com.main.sellplatform.service.LotService;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Date;

@Objtype(3)
public class Lot extends GeneralObject {


    @Reference(attributeId = 11, fetch = Reference.FetchType.EAGER)
    private User owner;

    @Attribute(attrTypeId = 12)
    private String name;


    @Attribute(attrTypeId = 14, number = true)
    private Double startPrice;


    @Attribute(attrTypeId = 15, number = true)
    private Double minPrice;

    private Double currentPrice;

    @Attribute(attrTypeId = 16, number = true)
    private Double endPrice;

    @Attribute(attrTypeId = 17)
    private LotStatus status;

    @Attribute(attrTypeId = 18)
    private String category;

    @Attribute(attrTypeId = 20)
    private String location;

    @Attribute(attrTypeId = 21, type = Attribute.ValueType.DATE_VALUE)
    private Date startDate;

    @Attribute(attrTypeId = 22, type = Attribute.ValueType.DATE_VALUE)

    private Date endDate;

    @Attribute(attrTypeId = 23, type = Attribute.ValueType.DATE_VALUE)

    private Date creationDate;

    @Attribute(attrTypeId = 25)
    private String imgPath;

    @Attribute(attrTypeId = 26)
    private String deliveryAddress;


    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
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

    public Double getEndPrice() {
        return endPrice;
    }

    public void setEndPrice(Double endPrice) {
        this.endPrice = endPrice;
    }

    public LotStatus getStatus() {
        return status;
    }

    public void setStatus(LotStatus status) {
        this.status = status;
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

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public Double countCurrentPrice() {
        Date today = new Date();
        if (today.getTime() > getEndDate().getTime()) {
            setCurrentPrice(getMinPrice());
            return getMinPrice();
        }
        if (today.getTime() < getStartDate().getTime()) {
            setCurrentPrice(getStartPrice());
            return getStartPrice();
        }
        long dateDiff = today.getTime() - getStartDate().getTime();
        long dayDiff = (dateDiff / (1000 * 60 * 60 * 24)) % 365;
        long period = ((getEndDate().getTime() - getStartDate().getTime()) / (1000 * 60 * 60 * 24)) % 365;
        double step = (getStartPrice() - getMinPrice()) / period;
        double curPrice = getStartPrice() - (dayDiff * step);
        setCurrentPrice(curPrice);
        return getCurrentPrice();
    }

    @Override
    public String toString() {
        return "Lot{" +
                "owner=" + owner +
                ", name='" + name + '\'' +
                ", startPrice=" + startPrice +
                ", minPrice=" + minPrice +
                ", endPrice=" + endPrice +
                ", status='" + status + '\'' +
                ", category='" + category + '\'' +
                ", location='" + location + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", creationDate=" + creationDate +
                ", imgPath='" + imgPath + '\'' +
                ", deliveryAddress='" + deliveryAddress + '\'' +
                ", id=" + id +
                '}';
    }

    public Double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(Double currentPrice) {
        this.currentPrice = currentPrice;
    }
}
