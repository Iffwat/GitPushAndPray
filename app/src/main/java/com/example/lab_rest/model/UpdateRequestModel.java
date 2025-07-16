package com.example.lab_rest.model;

public class UpdateRequestModel {
    private int requestId;
    private double weight;
    private String status;

    public UpdateRequestModel(int requestId, double weight, String status) {
        this.requestId = requestId;
        this.weight = weight;
        this.status = status;
    }

    public int getRequestId() { return requestId; }
    public double getWeight() { return weight; }
    public String getStatus() { return status; }
}
