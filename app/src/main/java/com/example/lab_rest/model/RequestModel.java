// File: RequestModel.java
package com.example.lab_rest.model;

import com.google.gson.annotations.SerializedName;

public class RequestModel {

    @SerializedName("request_id")
    private int requestId;

    @SerializedName("user_id")
    private int userId;

    private User user;

    @SerializedName("item_id")
    private int itemId;

    private RecyclableItem item;

    @SerializedName("item_name")
    private String itemName;

    @SerializedName("username")
    private String username;

    @SerializedName("address")
    private String address;

    @SerializedName("notes")
    private String notes;

    @SerializedName("status")
    private String status;

    @SerializedName("price_per_kg")
    private double pricePerKg;

    @SerializedName("weight") // ✅ Newly added field
    private double weight;

    private double totalPrice;

    // ✅ Getters
    public int getRequestId() { return requestId; }
    public int getUserId() { return userId; }
    public int getItemId() { return itemId; }
    public String getItemName() { return itemName; }
    public String getUsername() { return username; }
    public String getAddress() { return address; }
    public String getNotes() { return notes; }
    public String getStatus() { return status; }
    public double getPricePerKg() { return pricePerKg; }
    public double getWeight() { return weight; } // ✅ Added getter

    public double getTotalPrice(){
        return totalPrice;
    }

    // ✅ Setters
    public void setTotalPrice(double totalPrice){
        this.totalPrice = totalPrice;
    }
    public void setRequestId(int requestId) { this.requestId = requestId; }
    public void setUserId(int userId) { this.userId = userId; }
    public void setItemId(int itemId) { this.itemId = itemId; }
    public void setItemName(String itemName) { this.itemName = itemName; }
    public void setUsername(String username) { this.username = username; }
    public void setAddress(String address) { this.address = address; }
    public void setNotes(String notes) { this.notes = notes; }
    public void setStatus(String status) { this.status = status; }
    public void setPricePerKg(double pricePerKg) { this.pricePerKg = pricePerKg; }
    public void setWeight(double weight) { this.weight = weight; } // ✅ Added setter

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public RecyclableItem getItem() {
        return item;
    }

    public void setItem(RecyclableItem item) {
        this.item = item;
    }

}
