package com.example.afecommerce.Model;

import com.hishd.tinycart.model.Item;

import java.io.Serializable;
import java.math.BigDecimal;

public class Product{
    private String Category, Color, Gender, ImageURL, ProductTitle, ProductType, SubCategory, Usage, DateAdded;
    private Double ProductId;
    private Double Price;
    private Double quantity;
    private Double one_star,two_star,three_star,four_star,five_star;

    public Product(){}

    public Product(String category, String color, String gender, String imageURL, String productTitle, String productType, String subCategory, String usage, String dateAdded, Double productId, Double price, Double quantity, Double one_star, Double two_star, Double three_star, Double four_star, Double five_star) {
        Category = category;
        Color = color;
        Gender = gender;
        ImageURL = imageURL;
        ProductTitle = productTitle;
        ProductType = productType;
        SubCategory = subCategory;
        Usage = usage;
        DateAdded = dateAdded;
        ProductId = productId;
        Price = price;
        this.quantity = quantity;
        this.one_star = one_star;
        this.two_star = two_star;
        this.three_star = three_star;
        this.four_star = four_star;
        this.five_star = five_star;
    }

    public Product(String category, String color, String gender, String imageURL, String productTitle, String productType, String subCategory, String usage, String dateAdded, Double productId, Double price, Double one_star, Double two_star, Double three_star, Double four_star, Double five_star) {
        Category = category;
        Color = color;
        Gender = gender;
        ImageURL = imageURL;
        ProductTitle = productTitle;
        ProductType = productType;
        SubCategory = subCategory;
        Usage = usage;
        DateAdded = dateAdded;
        ProductId = productId;
        Price = price;
        this.one_star = one_star;
        this.two_star = two_star;
        this.three_star = three_star;
        this.four_star = four_star;
        this.five_star = five_star;
    }

    public Product(String category, String color, String gender, String imageURL, String productTitle, String productType, String subCategory, String usage, String dateAdded, Double productId, Double price, Double quantity) {
        Category = category;
        Color = color;
        Gender = gender;
        ImageURL = imageURL;
        ProductTitle = productTitle;
        ProductType = productType;
        SubCategory = subCategory;
        Usage = usage;
        DateAdded = dateAdded;
        ProductId = productId;
        Price = price;
        this.quantity = quantity;
    }

    public Double getProductId() {
        return ProductId;
    }

    public void setProductId(Double productId) {
        ProductId = productId;
    }

    public Double getPrice() {
        return Price;
    }

    public void setPrice(Double price) {
        Price = price;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getColor() {
        return Color;
    }

    public void setColor(String color) {
        Color = color;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public String getProductTitle() {
        return ProductTitle;
    }

    public void setProductTitle(String productTitle) {
        ProductTitle = productTitle;
    }

    public String getProductType() {
        return ProductType;
    }

    public void setProductType(String productType) {
        ProductType = productType;
    }

    public String getSubCategory() {
        return SubCategory;
    }

    public void setSubCategory(String subCategory) {
        SubCategory = subCategory;
    }

    public String getUsage() {
        return Usage;
    }

    public void setUsage(String usage) {
        Usage = usage;
    }

    public String getDateAdded() {
        return DateAdded;
    }

    public void setDateAdded(String dateAdded) {
        DateAdded = dateAdded;
    }

    public Double getOne_star() {
        return one_star;
    }

    public void setOne_star(Double one_star) {
        this.one_star = one_star;
    }

    public Double getTwo_star() {
        return two_star;
    }

    public void setTwo_star(Double two_star) {
        this.two_star = two_star;
    }

    public Double getThree_star() {
        return three_star;
    }

    public void setThree_star(Double three_star) {
        this.three_star = three_star;
    }

    public Double getFour_star() {
        return four_star;
    }

    public void setFour_star(Double four_star) {
        this.four_star = four_star;
    }

    public Double getFive_star() {
        return five_star;
    }

    public void setFive_star(Double five_star) {
        this.five_star = five_star;
    }
}
