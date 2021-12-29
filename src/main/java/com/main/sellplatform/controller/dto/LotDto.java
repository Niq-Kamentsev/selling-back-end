package com.main.sellplatform.controller.dto;

import com.main.sellplatform.entitymanager.GeneralObject;
import com.main.sellplatform.entitymanager.annotation.Attribute;
import com.main.sellplatform.persistence.entity.Category;
import com.main.sellplatform.persistence.entity.Lot;
import com.main.sellplatform.persistence.entity.User;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.Date;

public class LotDto  {

    private int id;
    @NotEmpty
    @Size(min = 3, max = 50, message = "Incorrect title length")
    private String name;
    @NotNull
    private String category;
    @NotNull
    private String description;
    @Positive
    private Double startPrice;
    @Positive
    private Double endPrice;
    @NotNull
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private Date startDate;
    @NotNull
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private Date endDate;
    @NotNull
    private String location;
    private String photo;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getStartPrice() {
        return startPrice;
    }

    public void setStartPrice(Double startPrice) {
        this.startPrice = startPrice;
    }

    public Double getEndPrice() {
        return endPrice;
    }

    public void setEndPrice(Double endPrice) {
        this.endPrice = endPrice;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "LotDto{" +
                "name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                ", startPrice=" + startPrice +
                ", endPrice=" + endPrice +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", location='" + location + '\'' +
                ", photo='" + photo + '\'' +
                '}';
    }

    public Lot getLot(User owner){
        Lot lot = new Lot();
        lot.setName(this.name);
        lot.setOwner(owner);
        lot.setStartPrice(this.startPrice);
        lot.setEndPrice(this.endPrice);
        lot.setCategory(this.category);
        lot.setLocation(this.location);
        lot.setStartDate(this.startDate);
        lot.setEndDate(this.endDate);
        lot.setImgPath(this.photo);
        lot.setDescr(this.description);
        return lot;


    }
}
