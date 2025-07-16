package com.example.lab_rest.model;

public class User {
    private int id;
    private String username;
    private String name;
    private String role;
    private String email;   // Optional field
    private String token;   // Optional field

    // Getters
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getName() { return name; }
    public String getRole() { return role; }
    public String getEmail() { return email; }
    public String getToken() { return token; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setName(String name) { this.name = name; }
    public void setRole(String role) { this.role = role; }
    public void setEmail(String email) { this.email = email; }
    public void setToken(String token) { this.token = token; }
}
