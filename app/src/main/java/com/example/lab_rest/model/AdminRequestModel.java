package com.example.lab_rest.model;

public class AdminRequestModel {
    private int requestId;

    private int userId;
    private int itemId;
    private String itemName;
    private String username;
    private String address;
    private String notes;
    private String status;
    private double pricePerKg;

    // Getters
    public int getRequestId() { return requestId; }

    public int getUserId() { return userId; }

    public int getItemId() { return itemId; }

    public String getItemName() { return itemName; }
    public String getUsername() { return username; }
    public String getAddress() { return address; }
    public String getNotes() { return notes; }
    public String getStatus() { return status; }
    public double getPricePerKg() { return pricePerKg; }
}
