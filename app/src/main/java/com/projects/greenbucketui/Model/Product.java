package com.projects.greenbucketui.Model;

public class Product {

    String CategoryName;
    String id;
    String category_id;
    String product_name;
    String product_detail;
    String product_price;
    String product_image_short;
    String Quantity;
    int selectedQuantity;
    public Product() {
    }

    public Product(int selectedQuantity,String categoryName, String id, String category_id, String product_name, String product_detail, String product_price, String product_image_short, String quantity) {

        CategoryName = categoryName;
        this.id = id;
        this.category_id = category_id;
        this.product_name = product_name;
        this.product_detail = product_detail;
        this.product_price = product_price;
        this.product_image_short = "http://www.greenvegetablebucket.com/"+product_image_short;
        Quantity = quantity;
        this.selectedQuantity = selectedQuantity;
    }

    public int getSelectedQuantity() {
        return selectedQuantity;
    }

    public void setSelectedQuantity(int selectedQuantity) {
        this.selectedQuantity = selectedQuantity;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_detail() {
        return product_detail;
    }

    public void setProduct_detail(String product_detail) {
        this.product_detail = product_detail;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getProduct_image_short() {
        return product_image_short;
    }

    public void setProduct_image_short(String product_image_short) {
        this.product_image_short = product_image_short;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }
}
