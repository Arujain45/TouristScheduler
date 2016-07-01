package com.ericsson.project.tescheduler.Objects;

public class Price {
    private String category;
    private int price;

    public Price(String category, int price){
        this.category = category;
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}