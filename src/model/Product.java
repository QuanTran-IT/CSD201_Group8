/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author ADMIN
 */
public class Product {
    private String id;
    private String name;
    private String category;
    private double price;
    private String brand;
    private double rating;
    private int views;

    public Product(String id, String name, String category, double price, String brand, double rating, int views) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.brand = brand;
        this.rating = rating;
        this.views = views;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }
    @Override
    public String toString() {
        return String.format("ID: %-6s | Name: %-20s | Category: %-15s | Brand: %-10s | Price: $%.2f | Rating: %.1f* | Views: %d", 
                id, name, category, brand, price, rating, views);
    }
}
