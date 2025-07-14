package com.example.lab_rest.model;

import com.google.gson.annotations.SerializedName;

public class RequestHistoryModel {

    @SerializedName("request_id")
    private int requestId;

    @SerializedName("item_name")
    private String itemName;

    @SerializedName("address")
    private String address;

    @SerializedName("status")
    private String status;

    @SerializedName("total_price")
    private double totalPrice;

    public int getRequestId() { return requestId; }
    public String getItemName() { return itemName; }
    public String getAddress() { return address; }
    public String getStatus() { return status; }
    public double getTotalPrice() { return totalPrice; }
}
