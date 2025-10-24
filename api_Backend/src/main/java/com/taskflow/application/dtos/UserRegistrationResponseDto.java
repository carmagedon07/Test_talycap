package com.taskflow.application.dtos;

public class UserRegistrationResponseDto {
    private Long userId;
    private String email;
    private String name;
    private String message;
    
    // Constructors
    public UserRegistrationResponseDto() {}
    
    public UserRegistrationResponseDto(Long userId, String email, String name, String message) {
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.message = message;
    }
    
    // Getters and Setters
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
}