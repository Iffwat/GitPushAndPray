package com.example.lab_rest.model;

import com.google.gson.annotations.SerializedName;

public class RecyclableItem {

    @SerializedName("item_id")
    private int itemId;

    @SerializedName("item_name")
    private String itemName;

    @SerializedName("price_per_kg")
    private double pricePerKg;

    // Getters
    public int getItemId() {
        return itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public double getPricePerKg() {
        return pricePerKg;
    }

    // Setters
    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public void setItemName(String name) {
        this.itemName = name;
    }

    public void setPricePerKg(double price) {
        this.pricePerKg = price;
    }

    // Optional: Useful for Spinner to show item name
    @Override
    public String toString() {
        return itemName;
    }
}
