package com.example.lab_rest.model;

import com.google.gson.annotations.SerializedName;

public class RecyclableItem {

    @SerializedName("item_id")
    private int itemId;

    @SerializedName("item_name")
    private String itemName;

    @SerializedName("price_per_kg")
    private double pricePerKg;

    public int getItemId() {
        return itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public double getPricePerKg() {
        return pricePerKg;
    }

    // Optional: setters or constructors if needed
}
