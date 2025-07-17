package com.example.lab_rest.model;

public class RequestModel {

    private int requestId;
    private int userId;
    private int itemId;
    private String address;
    private String requestDate;
    private String status;
    private double weight;
    private double totalPrice;
    private String notes;

    public RequestModel(){ }
    public RequestModel(int requestId,int userId, int itemId, String address, String requestDate,
                        String status, double weight, double totalPrice, String notes) {
        this.requestId = requestId;
        this.userId = userId;
        this.itemId = itemId;
        this.address = address;
        this.requestDate = requestDate;
        this.status = status;
        this.weight = weight;
        this.totalPrice = totalPrice;
        this.notes = notes;
    }

    public int getRequestId() {
        return requestId;
    }

    public int getUserId() { return userId; }
    public int getItemId() { return itemId; }
    public String getAddress() { return address; }
    public String getRequestDate() { return requestDate; }
    public String getStatus() { return status; }
    public double getWeight() { return weight; }
    public double getTotalPrice() { return totalPrice; }
    public String getNotes() { return notes; }

    public void setRequestId(int requestId){ this.requestId = requestId;}
    public void setUserId(int userId) { this.userId = userId; }
    public void setItemId(int itemId) { this.itemId = itemId; }
    public void setAddress(String address) { this.address = address; }
    public void setRequestDate(String requestDate) { this.requestDate = requestDate; }
    public void setStatus(String status) { this.status = status; }
    public void setWeight(double weight) { this.weight = weight; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
    public void setNotes(String notes) { this.notes = notes; }
}
