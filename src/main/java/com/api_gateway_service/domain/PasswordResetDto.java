package com.api_gateway_service.domain;


public class PasswordResetDto {
    private String email;
    private String token;
    private String newPassword;

    // Default constructor
    public PasswordResetDto() {}

    // Constructor with all fields
    public PasswordResetDto(String email, String token, String newPassword) {
        this.email = email;
        this.token = token;
        this.newPassword = newPassword;
    }

    // Getters and setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
