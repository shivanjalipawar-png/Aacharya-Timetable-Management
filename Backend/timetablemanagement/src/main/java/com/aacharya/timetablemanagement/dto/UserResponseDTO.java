package com.aacharya.timetablemanagement.dto;

import com.aacharya.timetablemanagement.entity.Role;

public class UserResponseDTO {
    private  int userId;
    private String username;
    private Role role;

    public UserResponseDTO( int userId, String username, Role role){
        this.userId=userId;
        this.role=role;
        this.username=username;

    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public Role getRole() {
        return role;
    }
}
