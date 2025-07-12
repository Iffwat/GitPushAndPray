package com.example.lab_rest.model;

import com.google.gson.annotations.SerializedName;

public class RequestModel {

    @SerializedName("user_id")
    private int userId;

    @SerializedName("item_id")
    private int itemId;

    @SerializedName("address")
    private String address;

    @SerializedName("notes")
    private String notes;

    public RequestModel(int userId, int itemId, String address, String notes) {
        this.userId = userId;
        this.itemId = itemId;
        this.address = address;
        this.notes = notes;
    }

    // Optional: Getters/Setters if needed
}
