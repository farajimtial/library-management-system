package com.library.lms.dto;

import com.library.lms.enums.Role;

/**
 * UserDTO - Data Transfer Object for User
 * Used to transfer user data between layers
 */
public class UserDTO {
    private Long userId;
    private String username;
    private String password;
    private String email;
    private Role role;
    
    // Constructors
    public UserDTO() {}
    
    public UserDTO(Long userId, String username, String email, Role role) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.role = role;
    }
    
    // Getters and Setters
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public Role getRole() {
        return role;
    }
    
    public void setRole(Role role) {
        this.role = role;
    }
}