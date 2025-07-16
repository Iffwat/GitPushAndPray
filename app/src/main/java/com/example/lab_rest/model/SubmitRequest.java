package com.example.lab_rest.model;

public class SubmitRequest {

    private int userId;
    private int itemId;
    private String address;
    private String notes;

    public SubmitRequest(int userId, int itemId, String address, String notes) {
        this.userId = userId;
        this.itemId = itemId;
        this.address = address;
        this.notes = notes;
    }
}
