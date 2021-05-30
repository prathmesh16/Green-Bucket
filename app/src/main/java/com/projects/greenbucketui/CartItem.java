package com.projects.greenbucketui;

public class CartItem {
    public CartItem(int id,String name, String catagory, int price, int quantity, String image) {
        this.name = name;
        this.catagory = catagory;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
        this.id=id;
    }

    String name;
    String catagory;
    int price;
    int quantity;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    int id;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCatagory() {
        return catagory;
    }

    public void setCatagory(String catagory) {
        this.catagory = catagory;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    String image;

}
