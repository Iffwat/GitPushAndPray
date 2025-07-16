package com.example.lab_rest.model;

public class RequestHistoryModel {

    private int requestId;
    private String itemName;
    private String address;
    private String status;
    private double totalPrice;

    // Getters
    public int getRequestId() {
        return requestId;
    }

    public String getItemName() {
        return itemName;
    }

    public String getAddress() {
        return address;
    }

    public String getStatus() {
        return status;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    // Optional Setters if needed
    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
