package com.subdrive.apigateway.domain;

public class LoginDto {
    private String email;
    private String password;

    // Default constructor
    public LoginDto() {}

    // Constructor with all fields
    public LoginDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters and setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
