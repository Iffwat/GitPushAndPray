package com.example.lab_rest.model;

public class LoginResponse {

    private String status;
    private User user;
    private String message;

    public String getStatus() {
        return status;
    }

    public User getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }
}
