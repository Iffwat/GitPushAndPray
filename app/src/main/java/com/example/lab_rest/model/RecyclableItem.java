package com.example.lab_rest.model;

public class RecyclableItem {
    private int itemId;
    private String itemName;
    private double pricePerKg;

    public int getItemId() { return itemId; }
    public String getItemName() { return itemName; }
    public double getPricePerKg() { return pricePerKg; }

    public void setItemId(int itemId) { this.itemId = itemId; }
    public void setItemName(String itemName) { this.itemName = itemName; }
    public void setPricePerKg(double pricePerKg) { this.pricePerKg = pricePerKg; }
}


