package com.main.sellplatform.controller.dto.lotdto;

import java.util.List;

public class LotFilterDTO {
    private Double minPrice;
    private Double maxPrice;

    private List<Category> categories;

    private Column sortColumn;
    private Boolean asc;

    private String search;

    public Double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }

    public Double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public Column getSortColumn() {
        return sortColumn;
    }

    public void setSortColumn(Column sortColumn) {
        this.sortColumn = sortColumn;
    }

    public Boolean getAsc() {
        return asc;
    }

    public void setAsc(Boolean asc) {
        this.asc = asc;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }


    public enum Category {
        TOYS, ELECTRONIC_DEVICE, FURNITURE, DECORATION
    }

    public enum Column {
        NAME, PRICE, CATEGORY
    }
}
