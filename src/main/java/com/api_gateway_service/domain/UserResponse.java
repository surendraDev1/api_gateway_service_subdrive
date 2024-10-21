package com.api_gateway_service.domain;

public class UserResponse {
    private String id;
    private String username;
    private String email;
    // Other fields that represent user information

    // Constructor
    public UserResponse(String id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public UserResponse() {

    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // You might also want to override toString(), equals(), and hashCode()
}
